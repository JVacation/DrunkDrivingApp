package com.example.drunkdrivingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(email text primary key,business text,password text)");
        db.execSQL("Create table driver(fName text,lName text,vehicleRegistration text primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");

    }
    //inserting in database
    public boolean insert(String email,String business,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("business",business);
        contentValues.put("password",password);
        long ins = db.insert( "user" ,null,contentValues);
        if(ins==-1) return false;
        else return true;
    }
    // Check if email exists
    public Boolean chkemail(String email){
         SQLiteDatabase db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery("Select * from user where email=?",new String[]{email});
         if (cursor.getCount()>0) return false;
         else return true;
    }
    //checking the email and pass
    public Boolean emailpassword(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email=? and password=?", new String[]{email,password});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    public String businessname(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select business from user where email=?", new String[]{email});
        if(cursor.moveToFirst()) {
            String business = cursor.getString(cursor.getColumnIndex("business"));
            return business;
        }
        else return "Not found";
    }
    // add driver to database
    public boolean addDriver(String fName,String lName,String vehicleRegistration){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fName",fName);
        contentValues.put("lName",lName);
        contentValues.put("vehicleRegistration",vehicleRegistration);
        long ins = db.insert( "driver" ,null,contentValues);
        if(ins==-1) return false;
        else return true;
    }
    // View driver data
    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from driver", null);
        return cursor;
    }

    public Cursor retrieveDriver(String reg){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from driver where vehicleRegistration=?", new String[]{reg});
        return cursor;
    }

    public boolean removeDriver(String reg){
        SQLiteDatabase db = this.getWritableDatabase();
        long ins = db.delete("driver","vehicleRegistration=?",new String[]{reg});
        if(ins<=0) return false;
        else return true;
    }

    public void updatePassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("password",password);
        db.update("user",values, "email=?",new String[]{email});
    }



}
