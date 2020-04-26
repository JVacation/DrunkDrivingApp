package com.example.drunkdrivingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class showPicture extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String showImage = prefs.getString("showImage", "UNKNOWN");
        String getDriver = prefs.getString("reg", "UNKNOWN");
        String rootPath = Environment.getExternalStorageDirectory() + "/Drivers/" + getDriver + "/" + showImage + ".jpg";
        File imgFile = new File(rootPath);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.imageView);
            myImage.setImageBitmap(myBitmap);
        }
        else{
            Toast.makeText(getApplicationContext(),"Image not found",Toast.LENGTH_SHORT).show();
        }


    }
}
