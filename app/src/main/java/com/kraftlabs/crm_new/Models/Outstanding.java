package com.kraftlabs.crm_new.Models;

/**
 * Created by ASHIK on 27-04-2017.
 */

public class Outstanding {
    private int id;
    private int customerCode;
    private int billNo;
    private String billDate;
    private long outstandingAMT;
    private String dueDays;
    private int serverId;


    public Outstanding() {
    }

    public Outstanding(int id, int customerCode, int billNo, String billDate, long outstandingAMT, String dueDays, int serverId) {
        this.id = id;
        this.customerCode = customerCode;
        this.billNo = billNo;
        this.billDate = billDate;
        this.outstandingAMT = outstandingAMT;
        this.dueDays = dueDays;
        this.serverId = serverId;
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

    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        customerCode = customerCode;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public float getOutstandingAMT() {
        return outstandingAMT;
    }

    public void setOutstandingAMT(long outstandingAMT) {
        this.outstandingAMT = outstandingAMT;
    }

    public String getDueDays() {
        return dueDays;
    }

    public void setDueDays(String dueDays) {
        this.dueDays = dueDays;
    }
}
