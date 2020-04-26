package com.example.drunkdrivingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddDriver extends AppCompatActivity {
    DatabaseHelper db;
    Button nextStep;
    EditText e1,e2,e3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        db = new DatabaseHelper(this);
        e1=(EditText)findViewById(R.id.fName);
        e2=(EditText)findViewById(R.id.lName);
        e3=(EditText)findViewById(R.id.vehicleReg);
        nextStep = findViewById(R.id.button);

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                String s3 = e3.getText().toString();
                if (s1.equals("") || s2.equals("") || s3.equals("")){
                    Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean addDriver = db.addDriver(s1,s2,s3);
                    if (addDriver == true){
                        Toast.makeText(getApplicationContext(),"Driver added successfully",Toast.LENGTH_SHORT).show();
                        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                        prefs.edit().putString("reg", s3).commit();
                        Intent intent = new Intent(AddDriver.this, AddDriverPictures.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Problem adding Driver. Please Check fields",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
