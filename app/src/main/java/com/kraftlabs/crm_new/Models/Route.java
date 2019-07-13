package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 3/1/17.
 */

public class Route {
    private int routeAssignId;
    private int routeId;
    private String routeName;
    private String startingLocation;
    private String createdUserName;
    private int createdUserId;
    private String date;
    private String status;

    public Route(int routeAssignId, int routeId, String routeName, String startingLocation, String createdUserName, int createdUserId, String date, String status) {
        this.routeAssignId = routeAssignId;
        this.routeId = routeId;
        this.routeName = routeName;
        this.startingLocation = startingLocation;
        this.createdUserName = createdUserName;
        this.createdUserId = createdUserId;
        this.date = date;
        this.status = status;
    }

    public Route() {

    }

    public int getRouteAssignId() {
        return routeAssignId;
    }

    public void setRouteAssignId(int routeAssignId) {
        this.routeAssignId = routeAssignId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(String startingLocation) {
        this.startingLocation = startingLocation;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public int getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(int createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
