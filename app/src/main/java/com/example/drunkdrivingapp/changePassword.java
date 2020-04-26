package com.example.drunkdrivingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changePassword extends AppCompatActivity {
    DatabaseHelper db;
    EditText e1, e2, e3;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        db = new DatabaseHelper(this);
        e1=(EditText)findViewById(R.id.pass1);
        e2=(EditText)findViewById(R.id.pass2);
        e3=(EditText)findViewById(R.id.pass3);
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        final String getEmail = prefs.getString("Email", "UNKNOWN");
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                String s3 = e3.getText().toString();
                if (s1.equals("") || s2.equals("") || s3.equals("")){
                    Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(s2.equals(s3)) {
                        Boolean check = db.emailpassword(getEmail,s1);
                        if(check==true){
                            db.updatePassword(getEmail,s2);
                            Toast.makeText(getApplicationContext(),"Password successfully changed",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Current password not correct",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
