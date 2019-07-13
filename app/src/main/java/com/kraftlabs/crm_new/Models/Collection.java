package com.kraftlabs.crm_new.Models;

/**
 * Created by ASHIK on 27-04-2017.
 */

public class Collection {
    private int id;
    private String paymentModeNo;
    private String paymentMode;
    private int amount;
    private String date;
    private int customerId;
    private String userId;

    private int serverCollectionId;

    public Collection() {
    }

    public Collection(int id, String paymentModeNo, String paymentMode, int amount, String date, int customerId, String userId, int serverCollectionId) {
        this.id = id;
        this.paymentModeNo = paymentModeNo;
        this.paymentMode = paymentMode;
        this.amount = amount;
        this.date = date;
        this.customerId = customerId;
        this.userId = userId;
        this.serverCollectionId = serverCollectionId;

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerCollectionId() {
        return serverCollectionId;
    }

    public void setServerCollectionId(int serverCollectionId) {
        this.serverCollectionId = serverCollectionId;
    }

    public String getPaymentModeNo() {
        return paymentModeNo;
    }

    public void setPaymentModeNo(String paymentModeNo) {
        this.paymentModeNo = paymentModeNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
