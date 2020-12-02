package com.example.messenger_app.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger_app.R;
import com.example.messenger_app.Shared.UserDTO;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.NoteHolder> {

    // Rewriting adapter for new friends list...

    private List<UserDTO> users = new ArrayList<>();
    private Context context;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USER_ID = "userId";
    public static final String AUTHORIZATION = "authorization";
    private String userId;
    private String authorization;


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newfriend, parent, false);

        // getting context to be able to use SendAddRequest.addFriend
        context = parent.getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, 0);

        userId = sharedPreferences.getString(USER_ID, "");
        authorization = sharedPreferences.getString(AUTHORIZATION, "");

        return new NewFriendAdapter.NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        UserDTO currentUser = users.get(position);
        String name = currentUser.getFirstName() + " " + currentUser.getLastName();
        holder.textViewName.setText(name);
        holder.textVieWEmail.setText(currentUser.getEmail());
        holder.addfriend.setOnClickListener(v -> {
            try {
                SendAddRequest.addFriend(currentUser.getEmail(), userId, context, authorization);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            v.setEnabled(false);
        });
    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public void setNotes(List<UserDTO> users) {
        this.users = users;
        notifyDataSetChanged();
    }


    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textVieWEmail;
        private Button addfriend;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.add_friend_name);
            textVieWEmail = itemView.findViewById(R.id.add_friend_email);
            addfriend = itemView.findViewById(R.id.add_friend_button);

        }
    }
}
