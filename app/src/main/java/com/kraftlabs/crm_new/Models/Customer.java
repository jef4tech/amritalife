package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 12/1/16.
 */
public class Customer {
    private int customerId;
    private String customerName;
    private String customerCode;
    private String city;
    private String state;
    private String address;
    private String phone;
    private String type;
    private String areaId;
    private String divisionCode;
    private String apprTurnover;
    private String competitorBrand;
    private int noOfEmployees;
    private long locationId;
    private String date;
    private int serverId;
    private String category;
    private int isLocationValid;
    private int isSynced;
    private float latitude;
    private float longitude;
    private String deviceId;
    private int userId;
    private String provider;
    private int isGpsActive;
    private String versionName;

    public Customer() {
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getIsGpsActive() {
        return isGpsActive;
    }

    public void setIsGpsActive(int isGpsActive) {
        this.isGpsActive = isGpsActive;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public int getIsLocationValid() {
        return isLocationValid;
    }

    public void setIsLocationValid(int isLocationValid) {
        this.isLocationValid = isLocationValid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getApprTurnover() {
        return apprTurnover;
    }

    public void setApprTurnover(String apprTurnover) {
        this.apprTurnover = apprTurnover;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int severId) {
        this.serverId = serverId;
    }

    public String getCompetitorBrand() {
        return competitorBrand;
    }

    public void setCompetitorBrand(String competitorBrand) {
        this.competitorBrand = competitorBrand;
    }

    public int getNoOfEmployees() {
        return noOfEmployees;
    }

    public void setNoOfEmployees(int noOfEmployees) {
        this.noOfEmployees = noOfEmployees;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
