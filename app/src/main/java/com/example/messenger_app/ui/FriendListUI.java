package com.example.messenger_app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.messenger_app.R;
import com.example.messenger_app.Shared.ConstantValues;
import com.example.messenger_app.Shared.FriendDTO;
import com.example.messenger_app.service.FriendAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendListUI extends AppCompatActivity{

    // UI and volley lvl of friend list. Using runnable to update users every 2 seconds

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USER_ID = "userId";
    public static final String AUTHORIZATION = "authorization";
    private static final String URL = ConstantValues.URL;
    public static final String LOGGEDIN = "loggedIn";


    private String userId;
    private String authorization;

    private RequestQueue mQueue;
    private FriendAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list2);
        mQueue = Volley.newRequestQueue(this);

        getPrefs();
        setAlarmForNewMessages();


        RecyclerView recyclerView = findViewById(R.id.recycler_view_friends);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        adapter = new FriendAdapter();
        recyclerView.setAdapter(adapter);

        findAllFriends();

    }

    // Gets all related shared prefs
    private void getPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        userId = sharedPreferences.getString(USER_ID, "");
        authorization = sharedPreferences.getString(AUTHORIZATION, "");
    }

    // sends a request for all friends of current user to the database...
    private void findAllFriends(){
        String url = URL + "user/" + userId + "/friends";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<FriendDTO> friendList = new ArrayList<FriendDTO>();

                        for (int i = 0; i < response.length(); i++){
                            JSONObject jresponse = response.getJSONObject(i);
                            String firstName = jresponse.getString("firstName");
                            String lastName = jresponse.getString("lastName");
                            String friendEmail = jresponse.getString("friendEmail");
                            String friendUserId = jresponse.getString("friendUserId");
                            long id = jresponse.getInt("id");
                            friendList.add(new FriendDTO(id, firstName, lastName, friendEmail, friendUserId));

                        }
                        adapter.setNotes(friendList);



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

    // Methods override for the top buttons. Search for friends and logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_friend_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_icon: {
                Intent intent = new Intent(this, AddFriendsUI.class);
                startActivity(intent);
                return true;
            }
            case R.id.logout: {
                logout();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }


    // Logout button. Deletes shared prefs and send to LoginUI.
    private void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        sharedPreferences.edit().remove(USER_ID).apply();
        sharedPreferences.edit().remove(AUTHORIZATION).apply();
        sharedPreferences.edit().putBoolean(LOGGEDIN, false).apply();

        Intent intent = new Intent(this, LoginUI.class);
        startActivity(intent);
        finish();
    }

    // Forcing to not be able to get back to LoginUI unless logout pressed
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FriendListUI.class));
        finish();
    }


    // Implementing runnable to run every 2 seconds to update screen if new friend added or new message received...
    private Runnable updateMessagesRunnable;

    class Task implements Runnable {
        @Override
        public void run() {
            updateMessagesHandler.post(new Runnable() {
                @Override
                public void run() {
                    FriendListUI friendList = new FriendListUI();
                    findAllFriends();
                    updateMessagesHandler.postDelayed(this, 2000);
                    updateMessagesRunnable = this;
                }
            });
        }
    }

    private Handler updateMessagesHandler;
    public void setAlarmForNewMessages() {
        Log.d("UPDATED_MSGS", "Running a background thread for checking for new messages between the logged in user and the other user whose username is ");
        updateMessagesHandler = new Handler();
        new Thread(new FriendListUI.Task()).start();
    }

}