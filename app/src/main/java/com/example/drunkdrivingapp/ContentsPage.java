package com.example.drunkdrivingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class ContentsPage extends AppCompatActivity {

    Button addDriver, currentDriver, taxiNumbers, accountDetails, logOut;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView txtLocation;
    TextView business;
    private static final int PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_contents_page);
        business = findViewById(R.id.textView7);
        addDriver = findViewById(R.id.button3);
        currentDriver = findViewById(R.id.button4);
        taxiNumbers = findViewById(R.id.button5);
        accountDetails = findViewById(R.id.button6);
        logOut = findViewById(R.id.button7);
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String getBusiness = prefs.getString("Business", "UNKNOWN");
        business.setText(getBusiness);

        // Get location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
                requestPermissions(permission, PERMISSION_CODE);
            }
            else {
                getLocation();
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            getLocation();
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        }


        addDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsPage.this, AddDriver.class);
                startActivity(intent);
            }
        });

        currentDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsPage.this, CurrentDrivers.class);
                startActivity(intent);
            }
        });

        taxiNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsPage.this, Taxi.class);
                startActivity(intent);
            }
        });

        accountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsPage.this, AccountDetails.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsPage.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    //permission from popup was granted
                    getLocation();
                }
                else {
                    //permissions from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getLocation() {

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                            prefs.edit().putString("Latitude", String.valueOf(wayLatitude)).commit();
                            prefs.edit().putString("Longitude", String.valueOf(wayLongitude)).commit();
                        }
                    }
                });
    }

}
