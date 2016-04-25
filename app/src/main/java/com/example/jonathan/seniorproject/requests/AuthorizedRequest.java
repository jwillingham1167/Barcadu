package com.example.jonathan.seniorproject.requests;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jonathan.seniorproject.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthorizedRequest extends JsonObjectRequest{

    private final SharedPreferences preferences;

    public AuthorizedRequest(SharedPreferences preferences, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.preferences = preferences;
    }

    @Override
    public Map<String, String> getHeaders() {
        final Map<String, String> headerMap = new HashMap<>();
        final String authString =  preferences.getString(Constants.SHARED_PREF_CREDENTIALS_KEY, null);
        Log.d("AUTH", authString);
        headerMap.put("Authorization", authString);
        return headerMap;
    }
}
