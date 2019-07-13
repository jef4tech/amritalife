package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 16/7/16.
 */

public class Lead {
    private int id;
    private String name;
    private String phone;
    private String address;
    private String details;
    private String info;
    private String date;
    private int serverLeadId;

    public Lead(int id, String name, String phone, String address, String details, String info, String date) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.details = details;
        this.info = info;

        this.date = date;
    }

    public Lead() {

    }

    public int getServerLeadId() {
        return serverLeadId;
    }

    public void setServerLeadId(int serverLeadId) {
        this.serverLeadId = serverLeadId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}
