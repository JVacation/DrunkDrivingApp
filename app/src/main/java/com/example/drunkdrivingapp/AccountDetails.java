package com.example.drunkdrivingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AccountDetails extends AppCompatActivity {

    TextView businessTV, emailTV, police;
    Button button;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        mQueue = Volley.newRequestQueue(this);
        businessTV = findViewById(R.id.business);
        emailTV = findViewById(R.id.email);
        police = findViewById(R.id.police);
        button = findViewById(R.id.button10);
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String getBusiness = prefs.getString("Business", "UNKNOWN");
        String getEmail = prefs.getString("Email", "UNKNOWN");
        businessTV.setText(getBusiness);
        emailTV.setText(getEmail);

        jsonParse();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDetails.this, changePassword.class);
                startActivity(intent);
            }
        });
    }

    private void jsonParse() {
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String Latitude = prefs.getString("Latitude", "UNKNOWN");
        String Longitude = prefs.getString("Longitude", "UNKNOWN");
        Toast.makeText(this, Latitude, Toast.LENGTH_SHORT).show();
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+Latitude+","+Longitude+"&radius=1500&type=police&key=AIzaSyA93D4Wl0Eixb9VZhbac7xz64A6n83SQb8";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject result = jsonArray.getJSONObject(i);
                        String name = result.getString("name");
                        police.setText(name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

}
