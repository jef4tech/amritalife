package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 16/7/16.
 */

public class User {
  private String name;
  private int userId;
  private String role;
  private String photoURL;
  private int storeId;
  private double ta;
  private double da;
  private int roleId;

  public User(int userId, String name, String role, String photoURL) {
    this.name = name;
    this.userId = userId;
    this.role = role;
    this.photoURL = photoURL;
  }

  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  public double getTa() {
    return ta;
  }

  public void setTa(double ta) {
    this.ta = ta;
  }

  public double getDa() {
    return da;
  }

  public void setDa(double da) {
    this.da = da;
  }

  public User() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getPhotoURL() {
    return photoURL;
  }

  public void setPhotoURL(String photoURL) {
    this.photoURL = photoURL;
  }

  public int getStoreId() {
    return storeId;
  }

  public void setStoreId(int storeId) {
    this.storeId = storeId;
  }

  @Override public String toString() {
    return "User{" +
        "name='" + name + '\'' +
        ", userId=" + userId +
        ", role='" + role + '\'' +
        ", photoURL='" + photoURL + '\'' +
        ", storeId=" + storeId +
        ", ta=" + ta +
        ", da=" + da +
        ", roleId=" + roleId +
        '}';
  }
}
