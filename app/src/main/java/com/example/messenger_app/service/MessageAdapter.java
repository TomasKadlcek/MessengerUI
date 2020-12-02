package com.example.messenger_app.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger_app.R;
import com.example.messenger_app.Shared.MessageDTO;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Overriding Adapter to display messages correctly

    private List<MessageDTO> messages = new ArrayList<>();
    private String userId;

    // case 0 == message bubble for friend
    // case 2 == message bubble for user
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 0: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_friend, parent, false);
                return new MessageHolder0(itemView);
            }
            case 2: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_user, parent, false);
                return new MessageHolder2(itemView);
            }
            default:return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageDTO currentMessage = messages.get(position);

        switch (holder.getItemViewType()){
            case 0: {
                MessageHolder0 messageHolder0 = (MessageHolder0)holder;
                messageHolder0.textViewName.setText(currentMessage.getSenderName());
                messageHolder0.textVieWMessage.setText(currentMessage.getMessage());
                break;
            }
            case 2: {
                MessageHolder2 messageHolder2 = (MessageHolder2)holder;
                messageHolder2.textViewName.setText(currentMessage.getSenderName());
                messageHolder2.textVieWMessage.setText(currentMessage.getMessage());
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderUserId().equals(userId)){
            return 2;
        }
        else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public void setMessages(List<MessageDTO> messages, String userId){
        this.messages = messages;
        this.userId = userId;
        notifyDataSetChanged();
    }


    // One class per holder...
    class MessageHolder0 extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textVieWMessage;


        public MessageHolder0(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_friend_name);
            textVieWMessage = itemView.findViewById(R.id.text_view_friend_message);
        }
    }

    class MessageHolder2 extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textVieWMessage;


        public MessageHolder2(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_user_name);
            textVieWMessage = itemView.findViewById(R.id.text_view_user_message);
        }
    }
}
