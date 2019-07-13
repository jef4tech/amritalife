package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 12/1/16.
 */
public class RouteCustomer extends Customer {

    private int routeCustomerId;
    private int routeId;
    private int customerId;
    private String place;
    private float latitude;
    private float longitude;
    private int sortOrder;
    private String date;
    private String grade;

    public RouteCustomer() {

    }

    public RouteCustomer(int routeCustomerId, int routeId, int customerId, String place, float latitude, float longitude, int sortOrder, String date,String grade) {
        this.routeCustomerId = routeCustomerId;
        this.routeId = routeId;
        this.customerId = customerId;
        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sortOrder = sortOrder;
        this.date = date;
        this.grade=grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRouteCustomerId() {
        return routeCustomerId;
    }

    public void setRouteCustomerId(int routeCustomerId) {
        this.routeCustomerId = routeCustomerId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getRCLatitude() {
        return latitude;
    }

    public void setRCLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getRCLongitude() {
        return longitude;
    }

    public void setRCLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int order) {
        this.sortOrder = sortOrder;
    }
}
