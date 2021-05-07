package com.c2g4.SingHealthWebApp.Notifications;

import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Notifications")
public class NotificationsModel {
    @Id
    private int notification_id;
    private int creator_id;
    private String title;
    private String message;
    private Date create_date;
    private Date receipt_date;
    private Date end_date;
    private int to_role_ids;
    /* to_role_ids
            binary MSB LSB
            TAM
            111 = 7
    */

    public NotificationsModel(int notification_id, int creator_id, String title, String message, Date create_date, Date receipt_date, Date end_date, int to_role_ids) {
        this.notification_id = notification_id;
        this.creator_id = creator_id;
        this.title = title;
        this.message = message;
        this.create_date = create_date;
        this.receipt_date = receipt_date;
        this.end_date = end_date;
        this.to_role_ids = to_role_ids;
    }


    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getReceipt_date() {
        return receipt_date;
    }

    public void setReceipt_date(Date receipt_date) {
        this.receipt_date = receipt_date;
    }

    public int getTo_role_ids() {
        return to_role_ids;
    }

    public void setTo_role_ids(int to_role_ids) {
        this.to_role_ids = to_role_ids;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public boolean isForManager(){
        return (to_role_ids &1) == 1;
    }
    public boolean isForAuditor(){
        return (to_role_ids>>1 &1) == 1;
    }
    public boolean isForTenant(){
        return (to_role_ids>>2 &1) ==1;
    }
}