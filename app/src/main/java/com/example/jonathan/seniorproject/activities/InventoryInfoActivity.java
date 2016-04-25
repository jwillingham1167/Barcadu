package com.example.jonathan.seniorproject.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jonathan.seniorproject.Constants;
import com.example.jonathan.seniorproject.Item;
import com.example.jonathan.seniorproject.R;
import com.example.jonathan.seniorproject.requests.AuthorizedRequest;

import org.json.JSONObject;

/**
 * Created by Jonathan on 1/24/2016.
 */
public class InventoryInfoActivity extends BaseActivity {

    private static final String URL_STRING="https://sheetsu.com/apis/c1e73b11";
    public static final int UPDATE_CODE = 0;
    private Item currentItem;
    private JsonObjectRequest jsonObjectRequest;
    private TextView itemNameText, itemPriceText, itemWeightText, itemVendorText, itemQuantityText;
    private EditText upcEditText, updateQuantityText;
    private Button upcButton, updateCountButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpViews();
        setClicks();
    }

    private void setUpViews() {
        itemNameText = (TextView)findViewById(R.id.inventory_info_item_name_result);
        itemPriceText = (TextView)findViewById(R.id.inventory_info_item_price_result);
        itemWeightText = (TextView)findViewById(R.id.item_weight_result);
        itemVendorText = (TextView)findViewById(R.id.item_vendor_result);
        itemQuantityText = (TextView)findViewById(R.id.item_quantity_result);
        upcButton = (Button)findViewById(R.id.inventory_info_upc_button);
        upcEditText = (EditText)findViewById(R.id.inventory_info_enter_sku_text);
        updateCountButton = (Button)findViewById(R.id.update_quantity_button);
        updateQuantityText =(EditText)findViewById(R.id.update_quantity_text);
    }

    private void setItemTextFields(Item item) {
        //Create item from DB info
        itemNameText.setText(item.getItemName());
        itemPriceText.setText(item.getPriceString());
        itemWeightText.setText(item.getWeightString());
        itemVendorText.setText(item.getVendorName());
        itemQuantityText.setText(item.getQuantityString());
    }

    private void setClicks() {
        upcButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            final String upcString = upcEditText.getText().toString(); //edittext to char
            if (upcString.length() == 0 || !upcString.matches("\\d+")) {
                upcEditText.setError("This field can not be blank and must be digits");
                return;
            }

            final String queryString = String.format("%s?barcode=%s", Constants.BASE_GET_ITEM_URL, upcString);

            //Get info from DB
            if(jsonObjectRequest == null) {
                jsonObjectRequest = new AuthorizedRequest
                    (getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE), queryString, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            currentItem = new Item(response);
                            setItemTextFields(currentItem);

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error.networkResponse.statusCode == 404) {
                                upcEditText.setError("Item not found");
                            }
                            else {
                                Toast.makeText(InventoryInfoActivity.this, "Network Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }){

                };

                final RequestQueue requestQueue = MainMenuActivity.getMainRequestQueue(getApplicationContext());

                requestQueue.add(jsonObjectRequest);
                requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JSONObject>() {
                    @Override
                    public void onRequestFinished(Request<JSONObject> request) {
                        jsonObjectRequest = null;
                    }
                });
            }
            }

        });
        updateCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem == null) {
                    upcEditText.setError("Enter an item!");
                    return;
                }
                // When user clicks update fdjklfjklasdjklf
                final String countString = updateQuantityText.getText().toString();
                final int count = Integer.parseInt(countString);
                currentItem.setItemQuantity(count);
                final JSONObject itemJsonObject = currentItem.toJsonObject();

                if (jsonObjectRequest != null) {
                    Log.d("NO NEW REQUEST", "ALREADY HAVE JSON REQUEST, NOT CREATING A NEW ONE");
                    return;
                }
                jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, URL_STRING, itemJsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("JSON RESPONSE", response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("UPDATE JSON ERROR", error.getMessage());
                        }
                    });

                final RequestQueue requestQueue = MainMenuActivity.getMainRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);
                requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JSONObject>() {
                    @Override
                    public void onRequestFinished(Request<JSONObject> request) {
                        jsonObjectRequest = null;
                    }
                });
            }
        });
    }













    //TODO: Remove this from here, move to item update page
    // Do this on page for updating item
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == UPDATE_CODE) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                // The user picked a contact.
//                // The Intent's data Uri identifies which contact was selected.
//
//                // Do something with the contact here (bigger example below)
//                item = (Item)data.getSerializableExtra(CountActivity.ITEM_STRING);
//                setItemTextFields();
//
//            }
//        }
//    }

}
