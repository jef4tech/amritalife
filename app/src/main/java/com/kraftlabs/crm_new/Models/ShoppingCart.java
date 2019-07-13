package com.kraftlabs.crm_new.Models;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajith on 18/1/16.
 */
public class ShoppingCart {

  private static ArrayList<CartItem> cart;

  private static Customer mCustomer;

  public static Customer getCustomer() {
    return mCustomer;
  }

  public static void setCustomer(Customer customer) {
    mCustomer = customer;
  }

  public static void clearCustomer() {
    mCustomer = null;
  }

  public static ArrayList<CartItem> getCart() {
    if (cart == null) {
      cart = new ArrayList<CartItem>();
    }
    return cart;
  }

  public static void setCart(ArrayList<CartItem> cartItems) {
    cart = cartItems;
  }

  public static int itemIndex(int productId) {

    for (int i = 0; i < cart.size(); i++) {
      if (cart.get(i).getProductId() == productId) {
        return i;
      }
    }
    return -1;
  }

  public static float getTotal() {
    float total = 0;
    for (int i = 0; i < cart.size(); i++) {
      total += cart.get(i).getPrice() * cart.get(i).getQuantity();
    }
    return total;
  }

  public static List toJSON(Context context) {

    List nameValuePairs = new ArrayList<>(2);
    try {

      SharedPreferences prefs =
          context.getSharedPreferences("UserDetailPreference", Context.MODE_PRIVATE);
      String isSalesRep = prefs.getString("isSalesRep", "0");
      String isManager = prefs.getString("isManager", "0");
      String userStoreId = prefs.getString("storeId", "0");
      String isHOUser = prefs.getString("isHOUser", "0");
      String userId = prefs.getString("userId", "0");

      //            nameValuePairs.add(new BasicNameValuePair("storeId", mCustomer.mCustomerId));
      //            nameValuePairs.add(new BasicNameValuePair("areaId", mCustomer.mAreaId));
      //            nameValuePairs.add(new BasicNameValuePair("divisionCode", mCustomer.mDivisionCode));
      //            nameValuePairs.add(new BasicNameValuePair("isSalesRep", isSalesRep));
      //            nameValuePairs.add(new BasicNameValuePair("isManager", isManager));
      //            nameValuePairs.add(new BasicNameValuePair("userStoreId", userStoreId));
      //            nameValuePairs.add(new BasicNameValuePair("isHOUser", isHOUser));
      //            nameValuePairs.add(new BasicNameValuePair("userId", userId));
      //            nameValuePairs.add(new BasicNameValuePair("items", cartItemsToJSON()));

      return nameValuePairs;
    } catch (Exception e) {
      e.printStackTrace();
      return nameValuePairs;
    }
  }

  public static String cartItemsToJSON() {

    JSONArray cartItemsArray = new JSONArray();
    JSONObject cartItemsObject;
    try {
      for (int i = 0; i < cart.size(); i++) {
        cartItemsObject = new JSONObject();
        cartItemsObject.putOpt("productId", cart.get(i).getProductId());
        cartItemsObject.putOpt("productCode", cart.get(i).getProductCode());
        cartItemsObject.putOpt("productName", cart.get(i).getProductName());
        cartItemsObject.putOpt("category", cart.get(i).getCategory());
        cartItemsObject.putOpt("quantity", cart.get(i).getQuantity());
        cartItemsObject.putOpt("unitprice", cart.get(i).getPrice());
        cartItemsObject.putOpt("offerquantity", 0);
        cartItemsArray.put(cartItemsObject);
      }
      return cartItemsArray.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
}
