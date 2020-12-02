package com.example.messenger_app.service;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger_app.R;
import com.example.messenger_app.Shared.FriendDTO;
import com.example.messenger_app.ui.MessagesUI;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.NoteHolder> {

    // Overriding adapter to get list of friends...

    public static final String FRIEND_USER_ID = "friendUserId";

    private List<FriendDTO> friends = new ArrayList<>();

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend, parent, false);


        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        FriendDTO currentFriend = friends.get(position);
        String name = currentFriend.getFirstName() + " " + currentFriend.getLastName();
        holder.textViewName.setText(name);
        holder.textVieWEmail.setText(currentFriend.getFriendEmail());
    }


    @Override
    public int getItemCount() {
        return friends.size();
    }


    public void setNotes(List<FriendDTO> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }


    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textVieWEmail;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textVieWEmail = itemView.findViewById(R.id.text_view_email);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Intent intent = new Intent(v.getContext(), MessagesUI.class);

                intent.putExtra(FRIEND_USER_ID, friends.get(position).getFriendUserId());

                v.getContext().startActivity(intent);
            });
        }
    }

}
