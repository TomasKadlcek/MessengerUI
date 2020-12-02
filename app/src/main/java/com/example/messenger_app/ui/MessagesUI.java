package com.example.messenger_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.messenger_app.R;
import com.example.messenger_app.Shared.AddMessageDTO;
import com.example.messenger_app.Shared.ConstantValues;
import com.example.messenger_app.Shared.LoginDTO;
import com.example.messenger_app.Shared.MessageDTO;
import com.example.messenger_app.json.JsonLogin;
import com.example.messenger_app.service.MessageAdapter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesUI extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USER_ID = "userId";
    public static final String AUTHORIZATION = "authorization";
    public static final String FRIEND_USER_ID = "friendUserId";
    private static final String URL = ConstantValues.URL;


    private String userId;
    private String friendId;
    private String authorization;

    private RequestQueue mQueue;

    private MessageAdapter adapter;

    private EditText newMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        friendId = getIntent().getStringExtra(FRIEND_USER_ID);

        mQueue = Volley.newRequestQueue(this);
        newMessageText = findViewById(R.id.edit_text_type_message);

        getPrefs();
        setAlarmForNewMessages();


        RecyclerView recyclerView = findViewById(R.id.recycler_view_messages);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);

        adapter = new MessageAdapter();
        recyclerView.setAdapter(adapter);


        findAllMessages();

        Button sendMessage = findViewById(R.id.send_message_button);
        sendMessage.setOnClickListener(v -> {
            try {
                sendMessageLogic();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


    }


    // Volley request used for sending a new message.
    private void sendMessageLogic() throws JSONException {
        String validateTextInput = validateFields(newMessageText);

        if (validateTextInput != null) {
            AddMessageDTO messageDetails = new AddMessageDTO(validateTextInput, friendId);

            newMessageText.setText("");
            Gson gson = new Gson();
            String json = gson.toJson(messageDetails);
            JSONObject js = new JSONObject(json);
            String url = URL + "user/" + userId + "/messages";

            JsonObjectRequest request = new JsonObjectRequest(url, js, response -> Log.d("NEW_MESSAGE", "User sent new message;"), Throwable::printStackTrace) {
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


    // sends volley request to get all new messages and passes it to the adapter...
    private void findAllMessages() {
        String url = URL + "user/" + userId + "/messages/" + friendId;


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<MessageDTO> allMessages = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jresponse = response.getJSONObject(i);
                            String message = jresponse.getString("message");
                            String senderUserId = jresponse.getString("senderUserId");
                            String receiverUserId = jresponse.getString("receiverUserId");
                            String timestamp = jresponse.getString("timestamp");
                            String senderName = jresponse.getString("senderName");
                            String receiverName = jresponse.getString("receiverName");
                            allMessages.add(new MessageDTO(message, senderUserId, receiverUserId, timestamp, senderName, receiverName));

                        }
                        adapter.setMessages(allMessages, userId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace) {
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

    // validate if input message is not empty
    private String validateFields(EditText input) {
        String output = input.getText().toString().trim();

        if (output.isEmpty()) {
            return null;
        } else {
            input.setError(null);
            return output;
        }
    }

    // Get all needed shared prefs
    private void getPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        userId = sharedPreferences.getString(USER_ID, "");
        authorization = sharedPreferences.getString(AUTHORIZATION, "");
    }


    // Implementing runnable to update every second if a new message comes in.
    private Runnable updateMessagesRunnable;

    class Task implements Runnable {
        @Override
        public void run() {
            updateMessagesHandler.post(new Runnable() {
                @Override
                public void run() {
                    findAllMessages();
                    updateMessagesHandler.postDelayed(this, 1000);
                    updateMessagesRunnable = this;
                }
            });
        }
    }

    private Handler updateMessagesHandler;

    public void setAlarmForNewMessages() {
        Log.d("UPDATED_MSGS", "Running a background thread");
        updateMessagesHandler = new Handler();
        new Thread(new MessagesUI.Task()).start();
    }
}