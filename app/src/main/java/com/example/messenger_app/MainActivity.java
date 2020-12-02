package com.example.messenger_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.messenger_app.ui.FriendListUI;
import com.example.messenger_app.ui.LoginUI;
import com.example.messenger_app.ui.RegisterUI;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String REGISTERED = "registered";
    public static final String LOGGEDIN = "loggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPrefs();
    }

    // Checks preferences. If user already registered or logged in on device. Send to corresponding UI...
    private void checkPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if (sharedPreferences.getBoolean(LOGGEDIN, false)){
            Intent intent = new Intent(this, FriendListUI.class);
            startActivity(intent);
            finish();
        }
        else{
            if (sharedPreferences.getBoolean(REGISTERED, false)){
                Intent intent = new Intent(this, LoginUI.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(this, RegisterUI.class);
                startActivity(intent);
                finish();
            }
        }
    }
}