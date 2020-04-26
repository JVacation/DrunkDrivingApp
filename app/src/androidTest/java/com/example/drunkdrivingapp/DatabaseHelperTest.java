package com.example.drunkdrivingapp;

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
public class DatabaseHelperTest {

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
    public void correctemailpassword() throws Exception {
        String CorrectEmail = "test@test.com";
        String CorrectPass = "test";
        Boolean test = db.emailpassword(CorrectEmail, CorrectPass);
        Assert.assertEquals(true, test);
    }

    @Test
    public void wrongemailpassword() throws Exception {
        String CorrectEmail = "wrong@test.com";
        String CorrectPass = "test";
        Boolean test = db.emailpassword(CorrectEmail, CorrectPass);
        Assert.assertEquals(true, test);
    }

    @Test
    public void emailwrongpassword() throws Exception {
        String CorrectEmail = "test@test.com";
        String CorrectPass = "wrong";
        Boolean test = db.emailpassword(CorrectEmail, CorrectPass);
        Assert.assertEquals(true, test);
    }

    @Test
    public void wrongemailwrongpassword() throws Exception {
        String CorrectEmail = "wrong@test.com";
        String CorrectPass = "wrong";
        Boolean test = db.emailpassword(CorrectEmail, CorrectPass);
        Assert.assertEquals(true, test);
    }
}