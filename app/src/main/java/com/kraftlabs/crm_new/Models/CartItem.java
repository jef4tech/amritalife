package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 18/1/16.
 */
public class CartItem {
    private int productId;
    private String productName;
    private String productCode;
    private String category;
    private int quantity;
    private Double price;

    public CartItem(int productId, String productName, String productCode, int quantity, Double price, String category) {
        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
