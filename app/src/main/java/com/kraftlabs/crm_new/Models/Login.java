package com.kraftlabs.crm_new.Models;

/**
 * Created by ashik on 12/8/17.
 */

public class Login {

    private int id;
    private String loginTime;
    private String logoutTime;
    private int serverId;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logOutTime) {
        this.logoutTime = logOutTime;
    }
}
