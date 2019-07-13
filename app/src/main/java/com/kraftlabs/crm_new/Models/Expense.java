package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 18/11/16.
 */

public class Expense {

    private int expenseId;
    private int userId;
    private int routeId;
    private String date;
    private String townVisited;
    private double da;
    private double ta;
    private String taType;
    private double taBus;
    private double taBikeKM;
    private double taBikeAmount;
    private double lodge;
    private double courier;
    private double sundries;
    private double total;
    private String createdDate;
    private String status;
    private int serverExpenseId = 0;
    private double freight;

    public Expense() {
    }

    public Expense(int expenseId, int userId, int routeId, String date, String townVisited, double da, double ta, String taType, double taBus, double taBikeKM, double taBikeAmount, double loadge, double courier, double sundries, double total, String createdDate, String status,double freight) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.routeId = routeId;
        this.date = date;
        this.townVisited = townVisited;
        this.da = da;
        this.ta = ta;
        this.taType = taType;
        this.taBus = taBus;
        this.taBikeKM = taBikeKM;
        this.taBikeAmount = taBikeAmount;
        this.lodge = loadge;
        this.courier = courier;
        this.sundries = sundries;
        this.total = total;
        this.createdDate = createdDate;
        this.status = status;
        this.freight=freight;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public int getServerExpenseId() {
        return serverExpenseId;
    }

    public void setServerExpenseId(int serverExpenseId) {
        this.serverExpenseId = serverExpenseId;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTownVisited() {
        return townVisited;
    }

    public void setTownVisited(String townVisited) {
        this.townVisited = townVisited;
    }

    public double getDa() {
        return da;
    }

    public void setDa(double da) {
        this.da = da;
    }

    public double getTa() {
        return ta;
    }

    public void setTa(double ta) {
        this.ta = ta;
    }

    public String getTaType() {
        return taType;
    }

    public void setTaType(String taType) {
        this.taType = taType;
    }

    public double getTaBus() {
        return taBus;
    }

    public void setTaBus(double taBus) {
        this.taBus = taBus;
    }

    public double getTaBikeKM() {
        return taBikeKM;
    }

    public void setTaBikeKM(double taBikeKM) {
        this.taBikeKM = taBikeKM;
    }

    public double getTaBikeAmount() {
        return taBikeAmount;
    }

    public void setTaBikeAmount(double taBikeAmount) {
        this.taBikeAmount = taBikeAmount;
    }

    public double getLodge() {
        return lodge;
    }

    public void setLodge(double lodge) {
        this.lodge = lodge;
    }

    public double getCourier() {
        return courier;
    }

    public void setCourier(double courier) {
        this.courier = courier;
    }

    public double getSundries() {
        return sundries;
    }

    public void setSundries(double sundries) {
        this.sundries = sundries;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
