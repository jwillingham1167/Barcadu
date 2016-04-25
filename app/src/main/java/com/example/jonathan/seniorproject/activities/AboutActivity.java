package com.example.jonathan.seniorproject.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.jonathan.seniorproject.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
