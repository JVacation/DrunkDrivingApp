package com.example.drunkdrivingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FilePermission;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDriverPictures extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    DatabaseHelper db;
    private Uri imagetoUploaduri;
    Uri image_uri;
    Button b1,b2,b3,b4;
    CheckBox confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver_pictures);
        db = new DatabaseHelper(this);
        b1 = findViewById(R.id.button2);
        b2 = findViewById(R.id.button9);
        b3 = findViewById(R.id.button);
        b4 = findViewById(R.id.button11);
        confirm = findViewById(R.id.checkBox);

        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        final String getReg = prefs.getString("reg", "UNKNOWN");
        final String rootPath = Environment.getExternalStorageDirectory() + "/Drivers/" +getReg;
        File mydir = new File(rootPath);
        if (!mydir.exists()){
            mydir.mkdirs();
        } else {
            Toast.makeText(this, "Exists", Toast.LENGTH_SHORT).show();
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        openCamera("driver", rootPath);
                    }
                }
                else {
                    openCamera("driver", rootPath);

                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        openCamera("keys", rootPath);
                    }
                }
                else {
                    openCamera("keys", rootPath);

                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(confirm.isChecked()) {
                    Toast.makeText(getApplicationContext(),"Driver Successfully Added",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddDriverPictures.this, ContentsPage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Please confirm the driver accepts Terms and Conditions",Toast.LENGTH_SHORT).show();
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                String getReg = prefs.getString("reg", "UNKNOWN");
                db.removeDriver(getReg);
                File dir = new File(Environment.getExternalStorageDirectory()+"/Drivers/" + getReg);
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                    dir.delete();
                }
                Intent intent = new Intent(AddDriverPictures.this, ContentsPage.class);
                Toast.makeText(getApplicationContext(),"Driver deleted",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    private void openCamera(String typeOfPhoto, String rootPath) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String photoName = getPhotoName();
        File f = new File(rootPath, typeOfPhoto+".jpg");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imagetoUploaduri = Uri.fromFile(f);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private String getPhotoName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "Image" + timestamp + ".jpg";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                final String getReg = prefs.getString("reg", "UNKNOWN");
                String rootPath = Environment.getExternalStorageDirectory() + "/Drivers/" +getReg;
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    //permission from popup was granted
                    openCamera("driver", rootPath);
                }
                else {
                    //permissions from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
