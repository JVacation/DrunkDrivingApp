package com.example.drunkdrivingapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperAddDriverTest {

    private DatabaseHelper db;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseHelper(getApplicationContext());
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testAddRemoveDriver() throws Exception {
        String testFName = "testFname";
        String testLName = "testLName";
        String testVReg = "testVReg";
        Boolean testAdd = db.addDriver(testFName, testLName, testVReg);
        Assert.assertEquals(true, testAdd);
        Boolean testDelete = db.removeDriver(testVReg);
        Assert.assertEquals(true, testDelete);
    }

}