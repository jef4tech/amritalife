package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 14/10/15.
 */
public class Order {

    private int id;
    private long orderId;
    private String orderNumber;
    private int customerId;
    private String customerName;
    private String customerCode;
    private Float grossAmount;
    private int userId;
    private String userName;
    private String orderDate;
    private String orderSyncDate;
    private String orderStatus;
    private int numberofItems;


    public Order(int id, long orderId, String orderNumber, int customerId, String customerName, String customerCode, Float grossAmount, int userId, String userName, String orderDate, String orderSyncDate, String orderStatus) {
        this.id = id;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerCode = customerCode;
        this.grossAmount = grossAmount;
        this.userId = userId;
        this.userName = userName;
        this.orderDate = orderDate;
        this.orderSyncDate = orderSyncDate;
        this.orderStatus = orderStatus;
    }

    public Order() {

    }

    public int getNumberofItems() {
        return numberofItems;
    }

    public void setNumberofItems(int numberofItems) {
        this.numberofItems = numberofItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Float getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Float grossAmount) {
        this.grossAmount = grossAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderSyncDate() {
        return orderSyncDate;
    }

    public void setOrderSyncDate(String orderSyncDate) {
        this.orderSyncDate = orderSyncDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
