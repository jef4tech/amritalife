package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 3/1/17.
 */

public class Message {
    private int messageAssignId;
    private int messageId;
    private String messageTitle;
    private String message;
    private String createdUserName;
    private int createdUserId;
    private String date;
    private String status;
    private int parentMessageId;


    public Message(int messageAssignId, int messageId, String messageTitle, String message, String createdUserName, int createdUserId, String date, int parentMessageId) {
        this.messageAssignId = messageAssignId;
        this.messageId = messageId;
        this.messageTitle = messageTitle;
        this.message = message;
        this.createdUserName = createdUserName;
        this.createdUserId = createdUserId;
        this.date = date;
        this.parentMessageId = parentMessageId;
    }

    public Message() {
    }

    public Message(int messageId, String messageTitle, String date, String message) {
        this.messageId = messageId;
        this.messageTitle = messageTitle;
        this.date = date;
        this.message = message;
    }

    public int getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(int parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public int getMessageAssignId() {
        return messageAssignId;
    }

    public void setMessageAssignId(int messageAssignId) {
        this.messageAssignId = messageAssignId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public int getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(int createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
