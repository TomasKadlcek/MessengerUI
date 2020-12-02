package com.example.messenger_app.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.messenger_app.Shared.AddFriendDTO;
import com.example.messenger_app.Shared.ConstantValues;
import com.example.messenger_app.Shared.LoginDTO;
import com.example.messenger_app.json.JsonLogin;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendAddRequest {

    private static final String URL = ConstantValues.URL;
    private static RequestQueue mQueue;

    public SendAddRequest(RequestQueue mQueue) {
        SendAddRequest.mQueue = mQueue;

    }

    // sending request to add a friend to friend list...

    public static void addFriend(String friendEmail, String userId, Context context, String authorization) throws JSONException {
        mQueue = Volley.newRequestQueue(context);

        AddFriendDTO userDetails = new AddFriendDTO(friendEmail);

        Gson gson = new Gson();
        String json = gson.toJson(userDetails);
        JSONObject js = new JSONObject(json);
        String url = URL + "user/" + userId + "/friends";

        JsonObjectRequest request = new JsonObjectRequest(url, js, response -> {
        }, Throwable::printStackTrace){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", authorization);
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };
        mQueue.add(request);
    }
}
