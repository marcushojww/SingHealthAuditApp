package com.c2g4.SingHealthWebApp.Chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Chat")
@AccessType(AccessType.Type.PROPERTY)
public class ChatModel {
    @Id
    private int chat_id;
    private int tenant_id;
    private int auditor_id;
    @Transient
    private JsonNode messages;

    public ChatModel(){}

    public ChatModel(int chat_id, int tenant_id, int auditor_id, JsonNode messages) {
        this.chat_id = chat_id;
        this.tenant_id = tenant_id;
        this.auditor_id = auditor_id;
        this.messages = messages;
    }

    public ChatModel(int chat_id, int tenant_id, int auditor_id, String messages) {
        this.chat_id = chat_id;
        this.tenant_id = tenant_id;
        this.auditor_id = auditor_id;
        ObjectMapper objectmapper = new ObjectMapper();
        try {
            this.messages = objectmapper.readTree(messages);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(int tenant_id) {
        this.tenant_id = tenant_id;
    }

    public int getAuditor_id() {
        return auditor_id;
    }

    public void setAuditor_id(int auditor_id) {
        this.auditor_id = auditor_id;
    }

    @Transient
    public JsonNode getMessages() {
        return messages;
    }
    @Transient
    public void setMessages(JsonNode messages) {
        this.messages = messages;
    }

    @JsonIgnore
    @Column(value="messages")
    public String get_messages_for_MySql() {
        ObjectMapper objectmapper = new ObjectMapper();
        try {
            return objectmapper.writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Column(value="messages")
    public void set_messages_for_MySql(String JsonString) {
        ObjectMapper objectmapper = new ObjectMapper();
        if(JsonString==null){
            this.messages = objectmapper.createObjectNode();
            return;
        }
        try {
            this.messages = objectmapper.readTree(JsonString);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
