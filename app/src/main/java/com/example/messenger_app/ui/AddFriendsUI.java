package com.example.messenger_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.messenger_app.R;
import com.example.messenger_app.Shared.ConstantValues;
import com.example.messenger_app.Shared.FriendDTO;
import com.example.messenger_app.Shared.SearchQuery;
import com.example.messenger_app.Shared.UserDTO;
import com.example.messenger_app.service.FriendAdapter;
import com.example.messenger_app.service.NewFriendAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFriendsUI extends AppCompatActivity {

    // UI level and Volley request for adding friends to friend list...

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USER_ID = "userId";
    public static final String AUTHORIZATION = "authorization";
    private static final String URL = ConstantValues.URL;

    private String userId;
    private String authorization;

    private RequestQueue mQueue;
    private NewFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        mQueue = Volley.newRequestQueue(this);

        getPrefs();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_add_friends);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        adapter = new NewFriendAdapter();
        recyclerView.setAdapter(adapter);

        try {
            findAll();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // sends request for all users not in friend list. Option to add friend in...
    private void findAll() throws JSONException {
        String url = URL + "user/" + userId + "/search";


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<UserDTO> userList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++){
                            JSONObject jresponse = response.getJSONObject(i);
                            String userId = jresponse.getString("userId");
                            String firstName = jresponse.getString("firstName");
                            String lastName = jresponse.getString("lastName");
                            String email = jresponse.getString("email");
                            userList.add(new UserDTO(userId, firstName, lastName, email));

                        }
                        adapter.setNotes(userList);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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


    // get preferences function returning userId and authorization from shared prefs...
    private void getPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        userId = sharedPreferences.getString(USER_ID, "");
        authorization = sharedPreferences.getString(AUTHORIZATION, "");
    }

}