package com.example.messenger_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import com.android.volley.toolbox.Volley;
import com.example.messenger_app.R;
import com.example.messenger_app.Shared.ConstantValues;
import com.example.messenger_app.Shared.RegisterDTO;
import com.example.messenger_app.json.JsonRegister;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterUI extends AppCompatActivity {

    RequestQueue mQueue;
    private static final String URL = ConstantValues.URL;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String REGISTERED = "registered";


    private TextInputLayout textInputFirstName;
    private TextInputLayout textInputLastName;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_u_i);
        mQueue = Volley.newRequestQueue(this);

        initializeFields();

        Button register = findViewById(R.id.button_register);
        Button login = findViewById(R.id.button_login);


        register.setOnClickListener(v -> {
            try {
                registerButtonAction();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


        login.setOnClickListener(v -> openLogin());
    }

    // uses validate fields and if all are filled sends jsonregisterrequest method...
    private void registerButtonAction() throws JSONException {
        String outgoingFirstName = validateFields(textInputFirstName);
        String outgoingLastName = validateFields(textInputLastName);
        String outgoingEmail = validateFields(textInputEmail);
        String outGoingPassword = validateFields(textInputPassword);

        if (outgoingFirstName != null && outgoingLastName != null && outgoingEmail != null && outGoingPassword != null) {
            jsonRegisterRequest(outgoingFirstName, outgoingLastName, outgoingEmail, outGoingPassword);
        }
    }

    // special register volley request.
    private void jsonRegisterRequest(String outgoingFirstName, String outgoingLastName, String outgoingEmail, String outGoingPassword) throws JSONException {
        RegisterDTO user = new RegisterDTO(outgoingFirstName, outgoingLastName, outgoingEmail, outGoingPassword);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        JSONObject js = new JSONObject(json);

        String url = URL + "user";
        JsonRegister request = new JsonRegister(url, js,
                response -> {
                    savePrefsData();
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    openLogin();
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error, please try again", Toast.LENGTH_LONG).show();

                });
        mQueue.add(request);
    }

    // Validate input fields and if empty error message
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

    // opens login intent...
    private void openLogin() {
        Intent intent = new Intent(this, LoginUI.class);
        startActivity(intent);
        finish();
    }

    // saving shared prefs if user registered. See main activity
    private void savePrefsData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(REGISTERED, true);

        editor.apply();
    }

    // Initializing all tech inputs...
    private void initializeFields() {
        textInputFirstName = findViewById(R.id.text_input_register_firstname);
        textInputLastName = findViewById(R.id.text_input_register_lastname);
        textInputEmail = findViewById(R.id.text_input_register_email);
        textInputPassword = findViewById(R.id.text_input_register_password);
    }
}