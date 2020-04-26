package com.example.drunkdrivingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;

public class Taxi extends AppCompatActivity {

    ArrayList<String> listItem, placeid;
    ArrayAdapter adapter;
    ListView userlist;
    String number;
    private RequestQueue mQueue;
    private RequestQueue pQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi);
        mQueue = Volley.newRequestQueue(this);
        pQueue = Volley.newRequestQueue(this);
        listItem = new ArrayList<>();
        placeid = new ArrayList<>();
        userlist = findViewById(R.id.users_list);
        jsonParse();
        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jsonParseNumber(position);
                }
            });
        }


    private void jsonParse() {
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String Latitude = prefs.getString("Latitude", "UNKNOWN");
        String Longitude = prefs.getString("Longitude", "UNKNOWN");
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+Latitude+","+Longitude+"&radius=1000&type=taxi%20service&keyword=taxi&key=AIzaSyA93D4Wl0Eixb9VZhbac7xz64A6n83SQb8";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject result = jsonArray.getJSONObject(i);
                        String name = result.getString("name");
                        String id = result.getString("place_id");
                        placeid.add(id);
                        listItem.add(name);
                    }
                    adapter = new ArrayAdapter(Taxi.this, R.layout.row, listItem);
                    userlist.setAdapter(adapter);
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

    private void jsonParseNumber(int pos) {
        String id = placeid.get(pos);
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+id+"&key=AIzaSyA93D4Wl0Eixb9VZhbac7xz64A6n83SQb8";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonArray = response.getJSONObject("result");
                    for (int i = 0; i < jsonArray.length(); i++){
                        number = jsonArray.getString("formatted_phone_number");
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Error 1",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + number));
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error 2",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        pQueue.add(request);
    }
}
