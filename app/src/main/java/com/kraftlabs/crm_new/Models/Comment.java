package com.kraftlabs.crm_new.Models;

/**
 * Created by ashik on 8/7/17.
 */

public class Comment {
    private int id;
    private int customerId;
    private int userId;
    private String comment;
    private String date;
    private int serverId;
    private String createdBy;
    private String status;
    private int callId;
    private int leadId;
    private int taskId;
    private int isDone;
    private int isRead;
    private String doneDate;
    private String taskCreatedBy;
    private String taskDoneBy;
    private String percentageOfCompleation;

    public Comment(int id, int customerId, int userId, String text, String date, int serverId, String createdBy, String status, int leadId, int taskId, int isDone, String doneDate, String taskCreatedBy, String taskDoneBy) {
        this.id = id;
        this.customerId = customerId;
        this.userId = userId;
        this.comment = text;
        this.date = date;
        this.serverId = serverId;
        this.createdBy = createdBy;
        this.status = status;
        this.leadId = leadId;
        this.taskId = taskId;
        this.isDone = isDone;
        this.doneDate = doneDate;
        this.taskCreatedBy = taskCreatedBy;
        this.taskDoneBy = taskDoneBy;
    }

/*
    KEY_ID,
    KEY_CUSTOMER_ID,
    KEY_CREATED_USER_ID,
    KEY_TEXT,
    KEY_DATE,
    KEY_SERVER_ID}*/


    public Comment(int id, int customerId, String comment, String date,  String createdBy,int serverId) {
        this.id = id;
        this.customerId = customerId;
        this.comment = comment;
        this.date = date;

        this.createdBy = createdBy;
        this.serverId = serverId;
    }

    public Comment() {

    }

    public int getCallId() {
        return callId;
    }

    public void setCallId(int callId) {
        this.callId = callId;
    }

    public String getPercentageOfCompleation() {
        return percentageOfCompleation;
    }

    public void setPercentageOfCompleation(String percentageOfCompleation) {
        this.percentageOfCompleation = percentageOfCompleation;
    }

    public int getLeadId() {
        return leadId;
    }

    public void setLeadId(int leadId) {
        this.leadId = leadId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCreatedUserId() {
        return userId;
    }

    public void setCreatedUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean isDone() {
        return isDone == 1;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public String getTaskCreatedBy() {
        return taskCreatedBy;
    }

    public void setTaskCreatedBy(String taskCreatedBy) {
        this.taskCreatedBy = taskCreatedBy;
    }

    public String getTaskDoneBy() {
        return taskDoneBy;
    }

    public void setTaskDoneBy(String taskDoneBy) {
        this.taskDoneBy = taskDoneBy;
    }


    public boolean isRead() {
        return isRead == 1;
    }

}
