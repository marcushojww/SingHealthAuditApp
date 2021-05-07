package com.c2g4.SingHealthWebApp.Chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;
import java.sql.Time;

@Table("ChatEntries")
@AccessType(AccessType.Type.PROPERTY)
public class ChatEntriesModel {
    @Id
    private int chat_entry_id;
    private Date date;
    private Time time;
    private int sender_id;
    private String subject;
    private String message_body;
    @Transient
    private JsonNode attachments;

    public ChatEntriesModel(){}

    public ChatEntriesModel(int chat_entry_id, Date date, Time time, int sender_id, String subject, String message_body, JsonNode attachments) {
        this.chat_entry_id = chat_entry_id;
        this.date = date;
        this.time = time;
        this.sender_id = sender_id;
        this.subject = subject;
        this.message_body = message_body;
        this.attachments = attachments;
    }

    public ChatEntriesModel(int chat_entry_id, Date date, Time time, int sender_id, String subject, String message_body, String attachments) {
        this.chat_entry_id = chat_entry_id;
        this.date = date;
        this.time = time;
        this.sender_id = sender_id;
        this.subject = subject;
        this.message_body = message_body;
        ObjectMapper objectmapper = new ObjectMapper();

        try {
            this.attachments = objectmapper.readTree(attachments);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }



    public int getChat_entry_id() {
        return chat_entry_id;
    }

    public void setChat_entry_id(int chat_entry_id) {
        this.chat_entry_id = chat_entry_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return message_body;
    }

    public void setMessageBody(String message_body) {
        this.message_body = message_body;
    }

    @Transient
    public JsonNode getAttachments() {
        return attachments;
    }
    @Transient
    public void setAttachments(JsonNode attachments) {
        this.attachments = attachments;
    }

    @Column("attachments")
    public String get_attachments_for_MySql() {
        ObjectMapper objectmapper = new ObjectMapper();
        try {
            return objectmapper.writeValueAsString(attachments);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Column(value="attachments")
    public void set_attachments_for_MySql(String JsonString) {
        ObjectMapper objectmapper = new ObjectMapper();
        if(JsonString==null){
            this.attachments =objectmapper.createObjectNode();
            return;
        }
        try {
            this.attachments = objectmapper.readTree(JsonString);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
