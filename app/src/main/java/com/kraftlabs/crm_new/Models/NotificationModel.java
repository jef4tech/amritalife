package com.kraftlabs.crm_new.Models;

/**
 * User: ashik
 * Date: 6/2/18
 * Time: 1:58 PM
 */
public class NotificationModel {
  private int userId;
  private String title;
  private String message;
  private String fragment;
  private String fragmentId;
  private String date;
  private int serverId;
  private  int id;

    public NotificationModel() {
    }

    public NotificationModel( int userId,
                              String title,
                              String message,
                              String fragment,
                              String fragmentId,
                              String date,
                              int serverId) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.fragment = fragment;
        this.fragmentId = fragmentId;
        this.date = date;
        this.serverId = serverId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(String fragmentId) {
        this.fragmentId = fragmentId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
