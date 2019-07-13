package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 19/1/17.
 */
public class Call extends RouteCustomer {
    /*
    * KEY_NEXT_VISIT_DATE =
KEY_REMARKS = "remark
KEY_PAYMENT_RECEIVED
KEY_ORDER_RECEIVED =
KEY_PRODUCT_DISCUSSED
*/
    private int callId;
    private int userId;
    private int routeId;
    private int routeCustomerId;
    private String status;
    private String date;
    // private String complaints = "";
    // private String collection = "";
    // private String promotionalProduct = "";
    // private String orderBooked = "";
    // private String informationConveyed = "";
    // private String stockAvailability = "";
    private String productDiscussed = "";
    // private String samplesGiven = "";      //next visited daate
    private int serverCallId;
    private int syncStatus = 0;
    private int commentCount;
    private String nextVisitDate = "";
    private String remarks = "";
    private double PaymentReceived = 0.0;
    private String OrderReceived = "";
    private String grade;
    private long locationId;
    private String deviceId;
    private String callDeviceId;
    private int callUserId;
    private float callLatitude;
    private float callLongitude;

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCallDeviceId() {
        return callDeviceId;
    }

    public void setCallDeviceId(String callDeviceId) {
        this.callDeviceId = callDeviceId;
    }

    public int getCallUserId() {
        return callUserId;
    }

    public void setCallUserId(int callUserId) {
        this.callUserId = callUserId;
    }

    public float getCallLatitude() {
        return callLatitude;
    }

    public void setCallLatitude(float callLatitude) {
        this.callLatitude = callLatitude;
    }

    public float getCallLongitude() {
        return callLongitude;
    }

    public void setCallLongitude(float callLongitude) {
        this.callLongitude = callLongitude;
    }

    @Override
    public long getLocationId() {
        return locationId;
    }

    @Override
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    @Override
    public String getGrade() {
        return grade;
    }

    @Override
    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getPaymentReceived() {
        return PaymentReceived;
    }

    public void setPaymentReceived(double paymentReceived) {
        PaymentReceived = paymentReceived;
    }

    public int getCallId() {
        return callId;
    }

    public void setCallId(int callId) {
        this.callId = callId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int getRouteId() {
        return routeId;
    }

    @Override
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    @Override
    public int getRouteCustomerId() {
        return routeCustomerId;
    }

    @Override
    public void setRouteCustomerId(int routeCustomerId) {
        this.routeCustomerId = routeCustomerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    public String getProductDiscussed() {
        return productDiscussed;
    }

    public void setProductDiscussed(String productDiscussed) {
        this.productDiscussed = productDiscussed;
    }

    public int getServerCallId() {
        return serverCallId;
    }

    public void setServerCallId(int serverCallId) {
        this.serverCallId = serverCallId;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getNextVisitDate() {
        return nextVisitDate;
    }

    public void setNextVisitDate(String nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOrderReceived() {
        return OrderReceived;
    }

    public void setOrderReceived(String orderReceived) {
        OrderReceived = orderReceived;
    }
}



