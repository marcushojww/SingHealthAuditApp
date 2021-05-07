package com.c2g4.SingHealthWebApp.Chat;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class ChatController {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private TenantRepo tenantRepo;
    @Autowired
    private AuditorRepo auditorRepo;
    @Autowired
    private ChatRepo chatRepo;
    @Autowired
    private ChatEntriesRepo chatEntriesRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * returns all the chats of the current user
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal
     * @return JsonArray of all chats with keys {chat_id, tenant_id, auditor_id, messages}
     * messages is a Json array of chatEntries IDs
     */
    @GetMapping("/chat/getAllChatsOfUser")
    public ResponseEntity<?> getAllChatsOfUser(@AuthenticationPrincipal UserDetails callerUser) {
        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount == null) {
            logger.warn("CALLER ACCOUNT NULL");
            return ResponseEntity.badRequest().body("user account not found");
        }
        List<ChatModel> allChats;
        if(callerAccount.getRole_id().equals(ResourceString.TENANT_ROLE_KEY)) {
            allChats = chatRepo.findChatsByTenantId(callerAccount.getAccount_id());
        } else if (callerAccount.getRole_id().equals(ResourceString.AUDITOR_ROLE_KEY)){
            allChats = chatRepo.findChatsByTAuditorId(callerAccount.getAccount_id());
        } else {
            return ResponseEntity.badRequest().body("user is not a tenant or auditor");
        }

        if (allChats == null || allChats.isEmpty()) return ResponseEntity.badRequest().body("no chats found");
        return ResponseEntity.ok(allChats);
    }


    /**
     * gets a JsonArray of the chat entries of the callerUser
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal
     * @param parentChatId an int, the id of the parent chat
     * @param numLastestChatEntries an optional int to indicate number of chat entries to return,
     *                              defaults to -1 and returns all chat entries if not specified
     * @return a JsonArray of Chat Entries with keys {chatEntry_id, date, time, sender_id, subject, messageBody, attachments;}
     */
    @GetMapping("/chat/getChatEntriesOfUser")
    public ResponseEntity<?> getChatEntriesOfUser(@AuthenticationPrincipal UserDetails callerUser,
                                                  @RequestParam int parentChatId, @RequestParam(required = false, defaultValue = "-1") int numLastestChatEntries) {

        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount == null) {
            logger.warn("CALLER ACCOUNT NULL");
            return ResponseEntity.badRequest().body("user account not found");
        }
        ChatModel chatModel = chatRepo.findByChatId(parentChatId);
        if(chatModel == null) return ResponseEntity.badRequest().body("no chat found");
        if(!checkChatAuthorization(chatModel,callerAccount)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, chat does not belong to user");
        }

        if(!chatModel.getMessages().has(ResourceString.CHAT_MESSAGES_JSON_KEY)){
            return ResponseEntity.badRequest().body("no chat entries found");
        }

        ArrayNode messageIds = (ArrayNode) chatModel.getMessages().get(ResourceString.CHAT_MESSAGES_JSON_KEY);
        numLastestChatEntries = numLastestChatEntries==-1? messageIds.size(): numLastestChatEntries;
        List<ChatEntriesModel> allChatEntries = new ArrayList<>();

        for(int i=numLastestChatEntries-1;i>=0;i--){
            ChatEntriesModel chatEntriesModel = chatEntriesRepo.findByChatEntryId(messageIds.get(i).asInt());
            if(chatEntriesModel!=null){
                allChatEntries.add(chatEntriesModel);
            }
        }
        if (allChatEntries.isEmpty()) return ResponseEntity.badRequest().body("no chat entries found");
        return ResponseEntity.ok(allChatEntries);
    }

    /**
     * creates a new chat for a particular auditor and tenant
     * @param auditor_id an int, the auditor_id
     * @param tenant_id an int, the tenant_id
     * @return HTTP Ok with the int chatModel ID if it is successfully created
     * returns HTTP bad request with message "{tenant/auditor} account not found"
     * if either ids do not correspond to an existing tenant/auditor
     */
    @PostMapping("/chat/postCreateNewChat")
    public ResponseEntity<?> postCreateNewChat(@RequestParam(value = "auditor_id", required = true) int auditor_id,
                                               @RequestParam(value = "tenant_id", required = true) int tenant_id){

        System.out.println("Testing");
        if(!tenantRepo.existsById(tenant_id)) return ResponseEntity.badRequest().body("tenant account not found");
        if(!auditorRepo.existsById(auditor_id)) return ResponseEntity.badRequest().body("auditor account not found");
        ChatModel existingChatmodel = chatRepo.findChatByUsers(auditor_id,tenant_id);
        if(existingChatmodel!=null) return ResponseEntity.badRequest().body("EXISTING:"+existingChatmodel.getChat_id());
        ChatModel chatModel = new ChatModel(0,tenant_id,auditor_id,objectMapper.createObjectNode());
        chatModel = chatRepo.save(chatModel);
        return ResponseEntity.ok(chatModel.getChat_id());
    }

    /**
     * saves a chat entry of the callerUser and updates the Parent Chat
     * @param callerUser the UserDetails of the caller taken from the Authentication Principal
     * @param parentChatId int, the id of the parent chat
     * @param messageContents String, the message contents of the chat with keys {subject,messageBody,attachments}
     * @return HTTP Ok the saved chat entry id as an int,
     * HTTP bad request if parent chat not found,
     * HTTP UNAUTHORIZED if the user does not have access to the chat
     */
    @PostMapping("/chat/postChatEntry")
    public ResponseEntity<?> postChatEntry(@AuthenticationPrincipal UserDetails callerUser,
                                           @RequestParam(value = "parentChatId") int parentChatId,
                                           @RequestPart(value = "messageContents") String messageContents){

        AccountModel callerAccount = convertUserDetailsToAccount(callerUser);
        if (callerAccount == null) {
            logger.warn("CALLER ACCOUNT NULL");
            return ResponseEntity.badRequest().body("user account not found");
        }
        ChatModel chatModel = chatRepo.findByChatId(parentChatId);
        if(chatModel==null) {
            return ResponseEntity.badRequest().body("no chats found");
        }
        if(!checkChatAuthorization(chatModel,callerAccount)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, chat does not belong to user");
        }

        try {
            ObjectNode messageContentsJson = (ObjectNode) objectMapper.readTree(messageContents);
            String subject = messageContentsJson.get("subject").asText();
            String messageBody = messageContentsJson.get("messageBody").asText();
            JsonNode attachments = messageContentsJson.get("attachments");
            int chatEntryId = saveChatEntry(chatModel,callerAccount.getAccount_id(),subject,messageBody,attachments);
            if(chatEntryId==Integer.MIN_VALUE) return ResponseEntity.badRequest().body("chat cannot be saved");
            return ResponseEntity.ok(chatEntryId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("cannot get contents from messageContents");
        }


    }

    /**
     *
     * @param chatModel ChatModel of the parent Chat
     * @param senderId int account_id of the callerAccount
     * @param subject String, the subject of the chat
     * @param messageBody String, the message body of the chat
     * @param attachments JsonNode, with JsonArray of attachment strings, key should be "attachments"
     * @return the saved chat entry id as an int
     */
    private int saveChatEntry(ChatModel chatModel, int senderId, String subject, String messageBody, JsonNode attachments) {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        Time time = new Time(Calendar.getInstance().getTime().getTime());
        //if(!attachments.has(ResourceString.CHAT_ENTRIES_ATTACHMENT_JSON_KEY)) throw new IllegalArgumentException();
        ChatEntriesModel chatEntriesModel = new ChatEntriesModel(0,date,time, senderId,subject,messageBody,attachments);
        chatEntriesModel = chatEntriesRepo.save(chatEntriesModel);
        try {
            updateParentChat(chatModel, chatEntriesModel.getChat_entry_id());
        } catch (JsonProcessingException e) {
            logger.warn("DELETING CHAT ENTRY NUM {} BECAUSE ENTRY CANNOT BE ADDED TO PARENT", chatEntriesModel.getChat_entry_id());
            chatEntriesRepo.delete(chatEntriesModel);
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
        return chatEntriesModel.getChat_entry_id();
    }

    /**
     * updates the parent chat messages field in the database
     * @param chatModel ChatModel of the parent Chat
     * @param chatEntryId int, id of the chat entry to add to the parent
     */
    private void updateParentChat(ChatModel chatModel, int chatEntryId) throws JsonProcessingException {
        ObjectNode messages = (ObjectNode) chatModel.getMessages();
        logger.info("messages from chatmodel {}",messages);
        if(messages==null) messages = objectMapper.createObjectNode();
        if(!messages.has(ResourceString.CHAT_MESSAGES_JSON_KEY)){
            ArrayNode arrayNode = objectMapper.createArrayNode();
            arrayNode.add(chatEntryId);
            messages.set(ResourceString.CHAT_MESSAGES_JSON_KEY,arrayNode);
            logger.info("messages no string {}",messages.get(ResourceString.CHAT_MESSAGES_JSON_KEY).get(0).asText());
        } else {
            ArrayNode keyNode = (ArrayNode) messages.get(ResourceString.CHAT_MESSAGES_JSON_KEY);
            keyNode.add(chatEntryId);
            logger.info("messages have string {}",messages.get(ResourceString.CHAT_MESSAGES_JSON_KEY).asText());
        }

        String messageString = objectMapper.writeValueAsString(messages);

        chatRepo.updateMessagesByChatId(chatModel.getChat_id(),messageString);
    }

    /**
     * checks whether the current user is authorized to access the chat
     * @param chatModel ChatModel of the chat to access
     * @param callerAccount AccountModel of the user
     * @return true if authorised, false otherwise
     */
    private boolean checkChatAuthorization(ChatModel chatModel, AccountModel callerAccount){
        return chatModel.getAuditor_id()==callerAccount.getAccount_id()
                || chatModel.getTenant_id() == callerAccount.getAccount_id();
    }

    /**
     * converts a UserDetails object into an AccountModel object
     * @param callerUser a UserDetail object
     * @return a corresponding AccountModel object
     */
    private AccountModel convertUserDetailsToAccount(UserDetails callerUser){
        logger.info("CALLER USER USERNAME {}",callerUser.getUsername());
        return accountRepo.findByUsername(callerUser.getUsername());
    }
}
