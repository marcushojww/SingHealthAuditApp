package com.c2g4.SingHealthWebApp.ControllersTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.c2g4.SingHealthWebApp.Chat.ChatEntriesModel;
import com.c2g4.SingHealthWebApp.Chat.ChatEntriesRepo;
import com.c2g4.SingHealthWebApp.Chat.ChatModel;
import com.c2g4.SingHealthWebApp.Chat.ChatRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ChatControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private AccountRepo accountRepo;
    @MockBean
    private ChatRepo chatRepo;
    @MockBean
    private ChatEntriesRepo chatEntriesRepo;
    @MockBean
    private TenantRepo tenantRepo;
    @MockBean
    private AuditorRepo auditorRepo;

    private static final String MANAGERUSENAME = "managerUsername";
    private static final String KNOWN_USER_PASSWORD = "test123";
    private static final String AUDITORUSENAME = "auditorUsername";
    private static final String TENANTUSENAME = "tenantUsername";

    private static final String MANAGER = "Manager";
    private static final String AUDITOR = "Auditor";
    private static final String TENANT = "Tenant";

    private static final int MANAGERID = 100;
    private static final int AUDITORID = 101;
    private static final int TENANTID = 102;

    private static final String statusOK = "ok";
    private static final String statusBad = "bad";
    private static final String statusUnauthorized = "unauthorized";

    @BeforeEach
    public void before() {
        AccountModel managerAccount = createAccount(MANAGER,MANAGERID,"Marcus","Ho","HQ");
        AccountModel auditorAccount = createAccount(AUDITOR,AUDITORID,"Hannah","Mah","Branch_A");
        AccountModel tenantAccount = createAccount(TENANT,TENANTID,"Gregory","Mah","Branch_A");
        given(accountRepo.findByUsername(MANAGERUSENAME)).willReturn(managerAccount);
        given(accountRepo.findByUsername(AUDITORUSENAME)).willReturn(auditorAccount);
        given(accountRepo.findByUsername(TENANTUSENAME)).willReturn(tenantAccount);
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllChatsOfUserTenantOK() throws Exception {
        getAllChatsOfUser(statusOK,"[{\"chat_id\":0,\"tenant_id\":102,\"auditor_id\":101," +
                "\"messages\":{\"messages\":[0,1,2]}},{\"chat_id\":0,\"tenant_id\":102,\"auditor_id\":101," +
                "\"messages\":{\"messages\":[0,1,2]}}, {\"chat_id\":0,\"tenant_id\":102,\"auditor_id\":101," +
                "\"messages\":{\"messages\":[0,1,2]}}]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllChatsOfUserAuditorOK() throws Exception {
        getAllChatsOfUser(statusOK,"[{\"chat_id\":0,\"tenant_id\":102,\"auditor_id\":101," +
                "\"messages\":{\"messages\":[0,1,2]}},{\"chat_id\":0,\"tenant_id\":102,\"auditor_id\":101," +
                "\"messages\":{\"messages\":[0,1,2]}}, {\"chat_id\":0,\"tenant_id\":102,\"auditor_id\":101," +
                "\"messages\":{\"messages\":[0,1,2]}}]");
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllChatsOfUserTenantBad() throws Exception {
        getAllChatsOfUser(statusBad,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllChatsOfUserAuditorBad() throws Exception {
        getAllChatsOfUser(statusBad,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllChatsOfUserManagerBad() throws Exception {
        getAllChatsOfUser(statusBad,null);
    }

    private void getAllChatsOfUser(String statusExpected, String compareJson) throws Exception {
        String url = "/chat/getAllChatsOfUser";

        switch (statusExpected) {
            case statusOK -> {
                List<ChatModel> chatModels = createChats();
                given(chatRepo.findChatsByTenantId(TENANTID)).willReturn(chatModels);
                given(chatRepo.findChatsByTAuditorId(AUDITORID)).willReturn(chatModels);
                HTTPRequestHelperTestFunctions.getHttpOk(mvc, url, null, 3, compareJson);
            }
            case statusBad -> HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc, url, null);
        }
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getChatEntriesOfUserTenantOK() throws Exception {
        getChatEntriesOfUser(statusOK,null,"0","[{\"chat_entry_id\":0,\"date\":\"2021-04-05\"," +
                "\"time\":\"14:20:30\",\"sender_id\":102,\"subject\":\"subject\",\"attachments\":" +
                "{\"attachments\":[\"df\"]},\"_attachments_for_MySql\":\"{\\\"attachments\\\":" +
                "[\\\"df\\\"]}\",\"messageBody\":\"message_body\"},{\"chat_entry_id\":0,\"date\":" +
                "\"2021-04-05\",\"time\":\"14:20:30\",\"sender_id\":102,\"subject\":\"subject\"," +
                "\"attachments\":{\"attachments\":[\"df\"]},\"_attachments_for_MySql\":" +
                "\"{\\\"attachments\\\":[\\\"df\\\"]}\",\"messageBody\":\"message_body\"}," +
                "{\"chat_entry_id\":0,\"date\":\"2021-04-05\",\"time\":\"14:20:30\"," +
                "\"sender_id\":102,\"subject\":\"subject\",\"attachments\":{\"attachments\"" +
                ":[\"df\"]},\"_attachments_for_MySql\":\"{\\\"attachments\\\":[\\\"df\\\"]}\"," +
                "\"messageBody\":\"message_body\"}]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getChatEntriesOfUserAuditorOK() throws Exception {
        getChatEntriesOfUser(statusOK,null,"0","[{\"chat_entry_id\":0,\"date\":\"2021-04-05\"," +
                "\"time\":\"14:20:30\",\"sender_id\":102,\"subject\":\"subject\",\"attachments\":" +
                "{\"attachments\":[\"df\"]},\"_attachments_for_MySql\":\"{\\\"attachments\\\":" +
                "[\\\"df\\\"]}\",\"messageBody\":\"message_body\"},{\"chat_entry_id\":0,\"date\":" +
                "\"2021-04-05\",\"time\":\"14:20:30\",\"sender_id\":102,\"subject\":\"subject\"," +
                "\"attachments\":{\"attachments\":[\"df\"]},\"_attachments_for_MySql\":" +
                "\"{\\\"attachments\\\":[\\\"df\\\"]}\",\"messageBody\":\"message_body\"}," +
                "{\"chat_entry_id\":0,\"date\":\"2021-04-05\",\"time\":\"14:20:30\"," +
                "\"sender_id\":102,\"subject\":\"subject\",\"attachments\":{\"attachments\"" +
                ":[\"df\"]},\"_attachments_for_MySql\":\"{\\\"attachments\\\":[\\\"df\\\"]}\"," +
                "\"messageBody\":\"message_body\"}]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getChatEntriesOfUserAuditorWithLimit() throws Exception {
        getChatEntriesOfUser(statusOK,"2","0","[{\"chat_entry_id\":0,\"date\":\"2021-04-05\"," +
                "\"time\":\"14:20:30\",\"sender_id\":102,\"subject\":\"subject\",\"attachments\":" +
                "{\"attachments\":[\"df\"]},\"_attachments_for_MySql\":\"{\\\"attachments\\\":" +
                "[\\\"df\\\"]}\",\"messageBody\":\"message_body\"},{\"chat_entry_id\":0,\"date\":" +
                "\"2021-04-05\",\"time\":\"14:20:30\",\"sender_id\":102,\"subject\":\"subject\"," +
                "\"attachments\":{\"attachments\":[\"df\"]},\"_attachments_for_MySql\":" +
                "\"{\\\"attachments\\\":[\\\"df\\\"]}\",\"messageBody\":\"message_body\"}]");
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getChatEntriesOfUserTenantNoChatFound() throws Exception {
        given(chatRepo.findByChatId(0)).willReturn(null);
        getChatEntriesOfUser(statusBad,null,"0",null);
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getChatEntriesOfUserTenantNoEntriesFound() throws Exception {
        ChatModel chatModel = new ChatModel(0,TENANTID,AUDITORID,"{\"messages\": [0, 1, 2]}");
        given(chatRepo.findByChatId(0)).willReturn(chatModel);
        given(chatEntriesRepo.findByChatEntryId(Mockito.any(Integer.class))).willReturn(null);
        getChatEntriesOfUser(statusBad,null,"0",null);
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getChatEntriesOfUserTenantNotBelong() throws Exception {
        ChatModel chatModel = new ChatModel(0,0,AUDITORID,"{\"messages\": [0, 1, 2]}");
        given(chatRepo.findByChatId(0)).willReturn(chatModel);
        getChatEntriesOfUser(statusUnauthorized,null,"0",null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getChatEntriesOfUserAuditorNotBelong() throws Exception {
        ChatModel chatModel = new ChatModel(0,TENANTID,0,"{\"messages\": [0, 1, 2]}");
        given(chatRepo.findByChatId(0)).willReturn(chatModel);
        getChatEntriesOfUser(statusUnauthorized,null,"0",null);

    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getChatEntriesOfUserManagerBad() throws Exception {
        getChatEntriesOfUser(statusBad,null,"0",null);
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getChatEntriesOfUserNoparentChatId() throws Exception {
        getChatEntriesOfUser(statusBad,null,null,null);
    }

    private void getChatEntriesOfUser(String statusExpected, String limit,String parentChatId, String compareJson) throws Exception {
        String url = "/chat/getChatEntriesOfUser";
        HashMap<String,String> params = new HashMap<>();
        if(parentChatId!=null) params.put("parentChatId",parentChatId);
        if(limit!=null) params.put("numLastestChatEntries",limit);

        switch (statusExpected) {
            case statusOK -> {
                ChatModel chatModel = new ChatModel(0,TENANTID,AUDITORID,"{\"messages\": [0, 1, 2]}");
                given(chatRepo.findByChatId(0)).willReturn(chatModel);
                Calendar c = Calendar.getInstance();
                c.set(2021,Calendar.APRIL,5,14,20,30);
                Date date = new Date(c.getTime().getTime());
                Time time = new Time(c.getTime().getTime());
                ChatEntriesModel chatEntriesModel = new ChatEntriesModel(0, date, time,
                        TENANTID, "subject", "message_body", "{\"attachments\":[\"df\"]}");
                given(chatEntriesRepo.findByChatEntryId(Mockito.any(Integer.class))).willReturn(chatEntriesModel);
                HTTPRequestHelperTestFunctions.getHttpOk(mvc, url, params, limit==null?3:Integer.parseInt(limit), compareJson);
            }
            case statusBad -> HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc, url, params);
            case statusUnauthorized -> HTTPRequestHelperTestFunctions.getHttpUnauthorizedRequest(mvc,url, params);
        }
    }


    //no auditor
    //no tenant
    //none
    @Test
    public void postCreateNewChatOK() throws Exception {
        given(tenantRepo.existsById(TENANTID)).willReturn(true);
        given(auditorRepo.existsById(AUDITORID)).willReturn(true);
        postCreateNewChat(statusOK,String.valueOf(AUDITORID),String.valueOf(TENANTID));
    }

    @Test
    public void postCreateNewChatBadAuditor() throws Exception {
        given(tenantRepo.existsById(TENANTID)).willReturn(true);
        given(auditorRepo.existsById(AUDITORID)).willReturn(false);
        postCreateNewChat(statusBad,String.valueOf(AUDITORID),String.valueOf(TENANTID));
    }

    @Test
    public void postCreateNewChatBadTenant() throws Exception {
        given(tenantRepo.existsById(TENANTID)).willReturn(false);
        given(auditorRepo.existsById(AUDITORID)).willReturn(true);
        postCreateNewChat(statusBad,String.valueOf(AUDITORID),String.valueOf(TENANTID));
    }

    @Test
    public void postCreateNewChatBadTenantAndAuditor() throws Exception {
        given(tenantRepo.existsById(TENANTID)).willReturn(false);
        given(auditorRepo.existsById(AUDITORID)).willReturn(false);
        postCreateNewChat(statusBad,String.valueOf(AUDITORID),String.valueOf(TENANTID));
    }

    @Test
    public void postCreateNewChatNoAuditor() throws Exception {
        given(tenantRepo.existsById(TENANTID)).willReturn(false);
        given(auditorRepo.existsById(AUDITORID)).willReturn(false);
        postCreateNewChat(statusBad,null,String.valueOf(TENANTID));
    }
    @Test
    public void postCreateNewChatNoTenant() throws Exception {
        given(tenantRepo.existsById(TENANTID)).willReturn(false);
        given(auditorRepo.existsById(AUDITORID)).willReturn(false);
        postCreateNewChat(statusBad,String.valueOf(AUDITORID),null);
    }
    @Test
    public void postCreateNewChatNoBody() throws Exception {
        given(tenantRepo.existsById(TENANTID)).willReturn(false);
        given(auditorRepo.existsById(AUDITORID)).willReturn(false);
        postCreateNewChat(statusBad,null,null);
    }

    private void postCreateNewChat(String statusExpected, String auditor_id, String tenant_id) throws Exception {
        String url = "/chat/postCreateNewChat";

        HashMap<String,String> params = new HashMap<>();
        if(auditor_id!=null) params.put("auditor_id",auditor_id);
        if(tenant_id!=null) params.put("tenant_id",tenant_id);

        switch (statusExpected) {
            case statusOK -> {
                when(chatRepo.save(Mockito.any(ChatModel.class))).thenAnswer(i -> {
                    Object[] args = i.getArguments();
                    ChatModel chatModel = (ChatModel) args[0];
                    chatModel.setChat_id(0);
                    return chatModel;
                });
                HTTPRequestHelperTestFunctions.postHttpOK(mvc,url,null,params,"0" ,true);
            }
            case statusBad -> HTTPRequestHelperTestFunctions.postHttpBadRequest(mvc,url, null,params);
        }
    }

    //parent ok chat missing arg

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void postChatEntryTenantOK() throws Exception {
        postChatEntry(statusOK,"0","subject","messageBody","[\"s\",\"as\"]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void postChatEntryAuditorOK() throws Exception {
        postChatEntry(statusOK,"0","subject","messageBody","[\"s\",\"as\"]");
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void postChatEntryTenantNotAuthorised() throws Exception {
        ChatModel chatModel = new ChatModel(0,0,AUDITORID,"{\"messages\": [0, 1, 2]}");
        given(chatRepo.findByChatId(0)).willReturn(chatModel);
        postChatEntry(statusUnauthorized,"0","subject","messageBody","[\"s\",\"as\"]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void postChatEntryAuditorNotAuthorised() throws Exception {
        ChatModel chatModel = new ChatModel(0,TENANTID,0,"{\"messages\": [0, 1, 2]}");
        given(chatRepo.findByChatId(0)).willReturn(chatModel);
        postChatEntry(statusUnauthorized,"0","subject","messageBody","[\"s\",\"as\"]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void postChatEntryAuditorParentNotFound() throws Exception {
        given(chatRepo.findByChatId(0)).willReturn(null);
        postChatEntry(statusBad,"0","subject","messageBody","[\"s\",\"as\"]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void postChatEntryAuditorNoParent() throws Exception {
        postChatEntry(statusBad,null,"subject","messageBody","[\"s\",\"as\"]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postChatEntryManagerBad() throws Exception {
        ChatModel chatModel = new ChatModel(0,TENANTID,AUDITORID,"{\"messages\": [0, 1, 2]}");
        given(chatRepo.findByChatId(0)).willReturn(chatModel);
        postChatEntry(statusUnauthorized,"0","subject","messageBody","[\"s\",\"as\"]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postChatEntryMissingArg() throws Exception {
        postChatEntry(statusBad,"0",null,"messageBody","[\"s\",\"as\"]");
    }

    private void postChatEntry(String statusExpected, String parentChatId, String subject,String messageBody,String attachments) throws Exception {
        String url = "/chat/postChatEntry";

        HashMap<String,String> postBody = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode body = objectMapper.createObjectNode();
        if(parentChatId!=null) params.put("parentChatId",parentChatId);
        if(subject!=null) body.put("subject",subject);
        if(messageBody!=null) body.put("messageBody",messageBody);
        if(attachments!=null) body.put("attachments",attachments);
        postBody.put("messageContents",objectMapper.writeValueAsString(body));

        switch (statusExpected) {
            case statusOK -> {
                ChatModel chatModel = new ChatModel(0,TENANTID,AUDITORID,"{\"messages\": [0, 1, 2]}");
                given(chatRepo.findByChatId(0)).willReturn(chatModel);
                when(chatEntriesRepo.save(Mockito.any(ChatEntriesModel.class))).thenAnswer(i -> {
                    Object[] args = i.getArguments();
                    ChatEntriesModel chatEntriesModel = (ChatEntriesModel) args[0];
                    chatEntriesModel.setChat_entry_id(0);
                    return chatEntriesModel;
                });
                HTTPRequestHelperTestFunctions.postHttpOK(mvc,url,postBody,params,"0" ,true);
            }
            case statusBad -> HTTPRequestHelperTestFunctions.postHttpBadRequest(mvc,url, postBody,params);
            case statusUnauthorized -> HTTPRequestHelperTestFunctions.postHttpUnauthorizedRequest(mvc,url, postBody,params);
        }
    }

    private AccountModel createAccount(String accountType, int acc_id, String firstName, String lastName, String branch_id){
        AccountModel newAccount = new AccountModel();
        newAccount.setAccount_id(acc_id);
        newAccount.setBranch_id(branch_id);
        newAccount.setEmail("something@email.com");
        newAccount.setEmployee_id(123);
        newAccount.setFirst_name(firstName);
        newAccount.setLast_name(lastName);
        newAccount.setHp("234");
        newAccount.setUsername(firstName+lastName);
        newAccount.setRole_id(accountType);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(KNOWN_USER_PASSWORD);
        newAccount.setPassword(password);

        return newAccount;
    }

    private List<ChatModel> createChats(){
        List<ChatModel> chatModels = new ArrayList<>();
        for(int i=0;i<3;i++){
            ChatModel chatModel = new ChatModel(0,TENANTID,AUDITORID,"{\"messages\": [0, 1, 2]}");
            chatModels.add(chatModel);
        }
        return chatModels;
    }

}