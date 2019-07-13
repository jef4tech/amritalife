package com.kraftlabs.crm_new.Models;

/**
 * Created by ajith on 14/10/15.
 */
public class Product {
    private String productName;
    private int productId;
    private String productCode;
    private String category;
    private String division;
    private String ingredients;
    private String indication;
    private String dosage;
    private String reference;
    private String anupana;
    private double insideKeralaPrice;
    private double outsideKeralaPrice;
    private String priceString = "";

    public Product(int productId, String productCode, String name, String category, String division, double insideKeralaPrice, double outsideKeralaPrice) {
        this.productName = name;
        this.productId = productId;
        this.productCode = productCode;
        this.category = category;
        this.division = division;

        this.insideKeralaPrice = insideKeralaPrice;
        this.outsideKeralaPrice = outsideKeralaPrice;
    }

    public Product(int productId, String name, String productCode, String category, String division, String ingredients, String indication, String dosage, String reference, String anupana, double insideKeralaPrice, double outsideKeralaPrice) {
        this.productName = name;
        this.productId = productId;

        this.productCode = productCode;
        this.category = category;
        this.division = division;
        this.ingredients = ingredients;
        this.indication = indication;
        this.dosage = dosage;
        this.reference = reference;
        this.anupana = anupana;
        this.insideKeralaPrice = insideKeralaPrice;
        this.outsideKeralaPrice = outsideKeralaPrice;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String name) {
        this.productName = name;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAnupana() {
        return anupana;
    }

    public void setAnupana(String anupana) {
        this.anupana = anupana;
    }

    public double getInsideKeralaPrice() {
        return insideKeralaPrice;
    }

    public void setInsideKeralaPrice(double insideKeralaPrice) {
        this.insideKeralaPrice = insideKeralaPrice;
    }

    public double getOutsideKeralaPrice() {
        return outsideKeralaPrice;
    }

    public void setOutsideKeralaPrice(double outsideKeralaPrice) {
        this.outsideKeralaPrice = outsideKeralaPrice;
    }

    public String getInfo(String type) {
        String mInfo;
        type = type.toLowerCase();
        switch (type) {
            case "ingredients":
                mInfo = this.ingredients;
                break;
            case "indications":
                mInfo = this.indication;
                break;
            case "dosage":
                mInfo = this.dosage;
                break;
            case "reference":
                mInfo = this.reference;
                break;
            case "anupana":
                mInfo = this.anupana;
                break;
            default:
                mInfo = "";
                break;
        }

        return mInfo;
    }
}
