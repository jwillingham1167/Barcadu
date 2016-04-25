package com.example.jonathan.seniorproject;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

//Class for creating items ---- temporary until DB


//TODO: DO NOT USE SERIALIZABLE, ONLY USE PARCELABLE
public class Item implements Serializable {

    private final static String ITEM_ID = "barcode"; //was id
    private final static String ITEM_NAME = "description";
    private final static String ITEM_PRICE = "price";
    private final static String ITEM_WEIGHT = "weight";
    private final static String VENDOR_NAME = "fk_vendor_id";
    private final static String ITEM_QUANTITY = "itemQuantity"; //N/A right now


    //public static final String JSON_ITEM_NAME = "itemName";

    private String id;
    private String itemName;
    private float itemPrice;
    private float itemWeight;
    private String vendorName;
    private int itemQuantity;

    public Item(String id, String itemName, int itemPrice, float itemWeight, String vendorName, int itemQuantity) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemWeight = itemWeight;
        this.vendorName = vendorName;
        this.itemQuantity = itemQuantity;
    }

    public Item(JSONObject object) {
        try{
            this.id = object.getString(ITEM_ID);
            this.itemName = object.getString(ITEM_NAME);
            final String itemPrice = object.getString(ITEM_PRICE);
            this.itemPrice = Float.valueOf(itemPrice == null || itemPrice.equals("null") ? "0" : itemPrice);
            final String itemWeight = object.getString(ITEM_WEIGHT);
            this.itemWeight = Float.valueOf(itemWeight == null || itemWeight.equals("null") ? "0" : itemWeight);
            this.vendorName = object.getString(VENDOR_NAME);
//            this.itemQuantity = Integer.valueOf(object.getString(ITEM_QUANTITY));
        }
        catch (JSONException e) {
            Log.e("JSON ITEM ERROR", e.getMessage());
        }
    }

    public String getPriceString() {
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        return n.format(itemPrice);
    }

    public String getWeightString() {
        return String.format(Locale.getDefault(), "%.2f lbs", itemWeight);
    }

    public String getQuantityString() {
        return ""+itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public float getItemWeight() {
        return itemWeight;
    }

    public String getVendorName() {
        return vendorName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public JSONObject toJsonObject() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ITEM_ID, id);
            jsonObject.put(ITEM_NAME, itemName);
            jsonObject.put(ITEM_PRICE, itemPrice);
            jsonObject.put(ITEM_WEIGHT, itemWeight);
            jsonObject.put(VENDOR_NAME, vendorName);
           // jsonObject.put(ITEM_QUANTITY, itemQuantity);
        } catch (JSONException e) {
            Log.e("ITEM JSON ERROR", e.getMessage());
        }

        return jsonObject;
    }
}