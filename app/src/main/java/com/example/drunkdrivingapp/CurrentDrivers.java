package com.example.drunkdrivingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CurrentDrivers extends AppCompatActivity {
    DatabaseHelper db;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    ListView userlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_drivers);

        db = new DatabaseHelper(this);
        listItem = new ArrayList<>();
        userlist = findViewById(R.id.users_list);
        viewDrivers();
        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = userlist.getItemAtPosition(position).toString();
                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                prefs.edit().putString("reg", text).commit();
                Intent intent = new Intent(CurrentDrivers.this, showDriver.class);
                startActivity(intent);
                finish();
;            }
        });
    }

    public void viewDrivers() {
        Cursor cursor = db.viewData();
        if (cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(),"No current Drivers",Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                listItem.add(cursor.getString(2));
            }
            adapter = new ArrayAdapter(this, R.layout.row, listItem);
            userlist.setAdapter(adapter);
        }
    }
}
