package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 18/11/16.
 */

public class Task {
    private int taskId;
    private String title;
    private String description;
    private int createdBy;
    private String createdDate;
    private int isRead;
    private int isDone;
    private String readDate;
    private String doneDate;
    private String createdUserName;
    private String photo;
    private int isSynced;


    public Task() {
    }

    public Task(int taskId, String title, String description, int createdBy, String createdDate, int isRead, int isDone, String readDate, String doneDate) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.isRead = isRead;
        this.isDone = isDone;
        this.readDate = readDate;
        this.doneDate = doneDate;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public boolean isSynced() {
        return isSynced == 1;
    }

    public boolean isDone() {
        return isDone == 1;
    }

    public boolean isRead() {
        return isRead == 1;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }
}
