/*
package com.kraftlabs.crm_new.AdditionalData.ImageData;

import com.kraftlabs.crm_new.Models.Customer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

*/
/**
 * User: ashik
 * Date: 27/9/17
 * Time: 2:23 PM
 *//*


public class CapturedImageModel {

    private static ArrayList<ImageModel> imageModels;
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static ArrayList<ImageModel> getImageModel() {
        if (imageModels == null) {
            imageModels = new ArrayList<ImageModel>();

        }
        return imageModels;

    }

    public static void setCart(ArrayList<ImageModel> imageItems) {
        imageModels = imageItems;
    }

    public static int itemIndex(int imageId) {

        for (int i = 0; i < imageModels.size(); i++) {
            if (imageModels.get(i).getId() == imageId) {
                return i;
            }
        }
        return -1;
    }

    public static String imageToJSON() {

        JSONArray imageArray = new JSONArray();
        JSONObject imageItemObject;
        try {
            for (int i = 0; i < imageModels.size(); i++) {
                imageItemObject = new JSONObject();
                imageItemObject.putOpt("imageId", imageModels.get(i).getId());
                imageItemObject.putOpt("productCode", imageModels.get(i).getProductCode());
                imageItemObject.putOpt("productName", imageModels.get(i).getProductName());
                imageItemObject.putOpt("category", imageModels.get(i).getCategory());
                imageItemObject.putOpt("quantity", imageModels.get(i).getQuantity());
                imageItemObject.putOpt("unitprice", imageModels.get(i).getPrice());
                imageItemObject.putOpt("offerquantity", 0);
                cartItemsArray.put(cartItemsObject);
            }
            return cartItemsArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
*/
