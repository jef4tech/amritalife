package com.kraftlabs.crm_new.AdditionalData.ImageData;

import java.util.Arrays;

/**
 * User: ashik
 * Date: 27/9/17
 * Time: 11:52 AM
 */

public class ImageModel {

    private int id;
    private int customerId;
    private String imageName;
    private byte[] image;
    private int serverId;
    private String date;
    private int callId;
    private long locationId;
    private int userId;
    

    public ImageModel(int id, int customerId, String imageName, byte[] image, int serverId, String date,long locationId,int userId) {
        this.id = id;
        this.customerId = customerId;
        this.imageName = imageName;
        this.image = image;
        this.serverId = serverId;
        this.date = date;
        this.locationId=locationId;
        this.userId=userId;
    }

    public ImageModel() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCallId() {
        return callId;
    }

    public void setCallId(int callId) {
        this.callId = callId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public ImageModel(byte[] imageInByte) {
        this.image = imageInByte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", imageName='" + imageName + '\'' +
                ", image=" + Arrays.toString(image) +
                ", serverId=" + serverId +
                ", date='" + date + '\'' +
                '}';
    }
}
