package com.kraftlabs.crm_new.Models;

/**
 * Created by ASHIK on 31-May-17.
 */

public class Supply {
    private int id;
    private String customerCode;
    private String orderNumber;
    private String skuCode;
    private int pendingQuantity;
    private int canceledQuantity;
    private int despatchQuantity;
    private String billNumber;
    private String date;
    private int serverId;


    public Supply(int id, String customerCode, String orderNumber, String skuCode, int pendingQuantity, int canceledQuantity, int despatchQuantity, String billNumber, String date, int serverId) {
        this.id = id;
        this.customerCode = customerCode;
        this.orderNumber = orderNumber;
        this.skuCode = skuCode;
        this.pendingQuantity = pendingQuantity;
        this.canceledQuantity = canceledQuantity;
        this.despatchQuantity = despatchQuantity;
        this.billNumber = billNumber;
        this.date = date;
        this.serverId = serverId;
    }

    public Supply() {

    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public int getPendingQuantity() {
        return pendingQuantity;
    }

    public void setPendingQuantity(int pendingQuantity) {
        this.pendingQuantity = pendingQuantity;
    }

    public int getCanceledQuantity() {
        return canceledQuantity;
    }

    public void setCanceledQuantity(int canceledQuantity) {
        this.canceledQuantity = canceledQuantity;
    }

    public int getDespatchQuantity() {
        return despatchQuantity;
    }

    public void setDespatchQuantity(int despatchQuantity) {
        this.despatchQuantity = despatchQuantity;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
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
}
