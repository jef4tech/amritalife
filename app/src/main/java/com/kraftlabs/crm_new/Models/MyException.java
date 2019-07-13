package com.kraftlabs.crm_new.Models;

/**
 * User: ashik
 * Date: 30/1/18
 * Time: 11:23 AM
 */

public class MyException {
    private int id;
    private int userId;
    private String exception;
    private String date;
    private int serverId;

    public MyException() {
    }

    public MyException(int id, int userId, String exception, String date, int serverId) {
        this.id = id;
        this.userId = userId;
        this.exception = exception;
        this.date = date;
        this.serverId = serverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
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

    @Override
    public String toString() {
        return "MyException{" +
                "id=" + id +
                ", userId=" + userId +
                ", exception='" + exception + '\'' +
                ", date='" + date + '\'' +
                ", serverId=" + serverId +
                '}';
    }
}
