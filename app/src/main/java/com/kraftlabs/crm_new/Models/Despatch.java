package com.kraftlabs.crm_new.Models;

/**
 * Created by ASHIK on 27-04-2017.
 */

public class Despatch {
    private int id;
    private String customerCode;
    private String orderNumber;
    private String orderDate;
    private String billNumber;
    private String billDate;
    private String itemCode;
    private String skuCode;
    private int itemCount;
    private float billValue;
    private String lrNo;
    private String lrDate;
    private int noOfBox;
    private String details;
    private int serverId;

    public Despatch() {
    }


    public Despatch(int id, String customerCode, String orderNumber, String orderDate, String billNumber, String billDate, String itemCode, String skuCode, int itemCount, float billValue, String lrNo, String lrDate, int noOfBox, String details, int serverId) {
        this.id = id;
        this.customerCode = customerCode;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.itemCode = itemCode;
        this.skuCode = skuCode;
        this.itemCount = itemCount;
        this.billValue = billValue;
        this.lrNo = lrNo;
        this.lrDate = lrDate;
        this.noOfBox = noOfBox;
        this.details = details;
        this.serverId = serverId;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDAte) {
        this.billDate = billDAte;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public float getBillValue() {
        return billValue;
    }

    public void setBillValue(float billValue) {
        this.billValue = billValue;
    }

    public String getLrNo() {
        return lrNo;
    }

    public void setLrNo(String lrNo) {
        this.lrNo = lrNo;
    }

    public String getLrDate() {
        return lrDate;
    }

    public void setLrDate(String lrDate) {
        this.lrDate = lrDate;
    }

    public int getNoOfBox() {
        return noOfBox;
    }

    public void setNoOfBox(int noOfBox) {
        this.noOfBox = noOfBox;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
