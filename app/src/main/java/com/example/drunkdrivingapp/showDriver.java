package com.example.drunkdrivingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class showDriver extends AppCompatActivity {

    DatabaseHelper db;
    TextView v1, v2, v3;
    String firstName, lastName, reg;
    Button delete, driverButton, keysButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_driver);
        db = new DatabaseHelper(this);
        v1 = findViewById(R.id.firstName);
        v2 = findViewById(R.id.lastName);
        v3 = findViewById(R.id.reg);
        delete = findViewById(R.id.button8);
        driverButton = findViewById(R.id.driverButton);
        keysButton = findViewById(R.id.keysButton);
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String getDriver = prefs.getString("reg", "UNKNOWN");
        showDriver(getDriver);
        v1.setText(firstName);
        v2.setText(lastName);
        v3.setText(reg);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.removeDriver(reg);
                File dir = new File(Environment.getExternalStorageDirectory()+"/Drivers/" + reg);
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                    dir.delete();
                }
                Intent intent = new Intent(showDriver.this, ContentsPage.class);
                Toast.makeText(getApplicationContext(),"Driver deleted",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(showDriver.this, showPicture.class);
                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                prefs.edit().putString("showImage", "driver").commit();
                startActivity(intent);
            }
        });

        keysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(showDriver.this, showPicture.class);
                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                prefs.edit().putString("showImage", "keys").commit();
                startActivity(intent);
            }
        });

    }

    public void showDriver(String registration) {
        Cursor cursor = db.retrieveDriver(registration);
        if (cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(),"No current Drivers",Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                firstName = cursor.getString(cursor.getColumnIndex("fName"));
                lastName = cursor.getString(cursor.getColumnIndex("lName"));
                reg = cursor.getString(cursor.getColumnIndex("vehicleRegistration"));
            }
        }
    }
}
