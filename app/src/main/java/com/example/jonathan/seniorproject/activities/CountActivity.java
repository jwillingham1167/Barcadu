package com.example.jonathan.seniorproject.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jonathan.seniorproject.Item;
import com.example.jonathan.seniorproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CountActivity extends BaseActivity {

    private final String URL_STRING = "https://sheetsu.com/apis/c1e73b11";
    private Item toUpdate;
    public static final String ITEM_STRING = "item";
    private Button updateCountButton, viewItemButton;
    private EditText updateCountText, upcEditText;
    private JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpViews();
        setClicks();
    }

    private void setUpViews() {
        updateCountButton = (Button) findViewById(R.id.count_page_update_quantity_button);
        viewItemButton= (Button)findViewById(R.id.count_page_upc_button);
        updateCountText = (EditText)findViewById(R.id.count_page_update_quantity_text);
        upcEditText = (EditText)findViewById(R.id.count_page_enter_sku_text);
    }

    private void setClicks() {
        updateCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user clicks update fdjklfjklasdjklf
                final String countString = updateCountText.getText().toString();
                final String upcString = upcEditText.getText().toString();

                jsonObjectRequest = new JsonObjectRequest
                    (URL_STRING, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                final JSONArray itemsArray = response.getJSONArray("result");
                                final int itemsLen = itemsArray.length();
                                boolean foundItem = false;
                                for (int i = 0; i < itemsLen; i++) {
                                    JSONObject curItem = itemsArray.getJSONObject(i);
                                    if (curItem.getString("id").equals(upcString)) {
                                        curItem.put("itemQuantity", countString);
                                        foundItem = true;
                                        break;
                                    }
                                }

                                if (!foundItem) {
                                    upcEditText.setError("Item does not exist!");
                                }

                            } catch (JSONException e) {
                                Log.e("JSON exception error!", e.getMessage());
                            } catch (NumberFormatException e) {
                                Log.e("Number format error!", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
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
}
