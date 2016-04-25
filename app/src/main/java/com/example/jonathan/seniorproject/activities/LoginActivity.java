package com.example.jonathan.seniorproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathan.seniorproject.Constants;
import com.example.jonathan.seniorproject.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    Button loginButton;
    EditText username, password;
    TextView notification;
    View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        if (preferences.contains(Constants.SHARED_PREF_CREDENTIALS_KEY)) {
            Log.d("FOUND IT", "FOUND THE CREDENTIALS");
            final Intent intent = new Intent(getApplicationContext(),InventoryInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        setUpViews();
        setClicks();
    }

    private void setUpViews() {
        //creates views
        loginButton = (Button)findViewById(R.id.loginButton);
        username = (EditText)findViewById(R.id.user_name_edit_text);
        password = (EditText)findViewById(R.id.password_edit_text);
        notification = (TextView)findViewById(R.id.passwordNotification);
        rootView = findViewById(R.id.root_view);
    }

    private void setClicks() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = username.getText().toString();
                final String pass = password.getText().toString();

                final String credentials = String.format("%s:%s", userName, pass);
                final String base64Credentials = Base64.encodeToString(
                        credentials.getBytes(),
                        Base64.DEFAULT);
                final String authenticationCredentials = String.format("Basic %s", base64Credentials);

                final Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Failed to sign in", error);
                        Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                    }
                };

                final Response.Listener<String> successListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
                        final SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Constants.SHARED_PREF_CREDENTIALS_KEY, authenticationCredentials);
                        editor.apply();
                        final Intent intent = new Intent(getApplicationContext(),InventoryInfoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                };

                final StringRequest checkUsernamePass = new StringRequest(Request.Method.GET, Constants.BASE_GET_ITEM_URL, successListener, errorListener) {

                    @Override
                    public Map<String, String> getHeaders() {
                        final Map<String, String> headerMap = new HashMap<String, String>();
                        headerMap.put("Authorization", String.format(authenticationCredentials));
                        return headerMap;
                    }
                };

                final RequestQueue requestQueue = MainMenuActivity.getMainRequestQueue(getApplicationContext());
                requestQueue.add(checkUsernamePass);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
