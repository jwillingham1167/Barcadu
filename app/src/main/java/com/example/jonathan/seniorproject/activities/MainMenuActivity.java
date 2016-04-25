package com.example.jonathan.seniorproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.jonathan.seniorproject.R;

/**
 * Created by Jonathan on 1/23/2016.
 */
public class MainMenuActivity extends BaseActivity {

    public static RequestQueue mainRequestQueue;

    Button inventoryInfoButton, countsButton, transfersButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainRequestQueue =  Volley.newRequestQueue(getApplicationContext());
        setContentView(R.layout.main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpViews();
        setClicks();
    }

    private void setUpViews() {
        //creates views
        inventoryInfoButton = (Button)findViewById(R.id.inventory_info_button);
        countsButton = (Button)findViewById(R.id.counts_button);
        transfersButton = (Button)findViewById(R.id.transfers_button);
    }

    private void setClicks() {
        inventoryInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InventoryInfoActivity.class);
                startActivity(intent);
            }
        });
        countsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(v.getContext(), CountActivity.class);
                startActivity(intent);
            }
        });
    }

    public static RequestQueue getMainRequestQueue(Context context) {
        if (mainRequestQueue == null) {
            mainRequestQueue = Volley.newRequestQueue(context);
        }
        return mainRequestQueue;
    }
}
