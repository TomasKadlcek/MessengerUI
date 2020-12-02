package com.example.messenger_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.messenger_app.R;
import com.example.messenger_app.Shared.ConstantValues;
import com.example.messenger_app.Shared.LoginDTO;
import com.example.messenger_app.json.JsonLogin;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginUI extends AppCompatActivity {

    // Logging UI and volley.

    RequestQueue mQueue;
    private static final String URL = ConstantValues.URL;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USER_ID = "userId";
    public static final String AUTHORIZATION = "authorization";
    public static final String LOGGEDIN = "loggedIn";

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_u_i);

        mQueue = Volley.newRequestQueue(this);

        initializeFields();

        Button login = findViewById(R.id.login_button_login);

        Button register = findViewById(R.id.login_button_register);


        // set on action for login button
        login.setOnClickListener(v -> {
            try {
                loginActionButton();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        // set on action for register button
        register.setOnClickListener(v -> openRegister());
    }

    // special volley request. Needs to get headers and save them to shared prefs
    private void loginActionButton() throws JSONException {
        String outgoingEmail = validateFields(textInputEmail);
        String outGoingPassword = validateFields(textInputPassword);

        if (outgoingEmail != null && outGoingPassword != null){

            LoginDTO userDetails = new LoginDTO(outgoingEmail, outGoingPassword);

            Gson gson = new Gson();
            String json = gson.toJson(userDetails);
            JSONObject js = new JSONObject(json);
            String url = URL + "login";

            JsonLogin request = new JsonLogin(url, js,
                    response -> {
                        try {
                            JSONObject headers = response.getJSONObject("headers");
                            String auth = headers.getString("Authorization");
                            String userID = headers.getString("UserID");

                            savePrefsData(auth, userID);
                            openFriendList(userID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace);

            mQueue.add(request);

        }
    }

    // make sure fields are not empty before submit
    private String validateFields(TextInputLayout text) {
        String output = text.getEditText().getText().toString().trim();

        if (output.isEmpty()) {
            text.setError("Cannot be empty.");
            return null;
        } else {
            text.setError(null);
            return output;
        }
    }


    // new intent register
    private void openRegister() {
        Intent intent = new Intent(this, RegisterUI.class);
        startActivity(intent);
    }

    // start friend list intent and finish login...
    private void openFriendList(String userId){
        Intent intent = new Intent(this, FriendListUI.class);

        intent.putExtra(USER_ID, userId);
        startActivity(intent);
        finish();
    }


    // initialize text inputs...
    private void initializeFields() {
        textInputEmail = findViewById(R.id.text_input_login_email);
        textInputPassword = findViewById(R.id.text_input_login_password);

    }

    // Save data to shared prefs before log in.
    private void savePrefsData(String auth, String userId){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_ID, userId);
        editor.putString(AUTHORIZATION, auth);
        editor.putBoolean(LOGGEDIN, true);

        editor.apply();

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}