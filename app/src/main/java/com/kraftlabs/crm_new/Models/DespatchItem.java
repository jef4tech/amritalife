package com.kraftlabs.crm_new.Models;

/**
 * Created by ASHIK on 02-Jun-17.
 */

public class DespatchItem {
    private String itemName;
    private String skuCode;
    private int quantity;
    private String lrNumber;
    private String lrDate;
    private float value;
    public DespatchItem() {
    }
    public DespatchItem(String itemName, String skuCode, int quantity) {
        this.itemName = itemName;
        this.skuCode = skuCode;
        this.quantity = quantity;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getLrNumber() {
        return lrNumber;
    }

    public void setLrNumber(String lrNumber) {
        this.lrNumber = lrNumber;
    }

    public String getLrDate() {
        return lrDate;
    }

    public void setLrDate(String lrDate) {
        this.lrDate = lrDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
