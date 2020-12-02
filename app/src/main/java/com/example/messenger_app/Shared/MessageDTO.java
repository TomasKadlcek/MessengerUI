package com.example.messenger_app.Shared;

import java.sql.Timestamp;

public class MessageDTO {

    private String message;
    private String senderUserId;
    private String receiverUserId;
    private String timestamp;
    private String senderName;
    private String receiverName;

    public MessageDTO(String message, String senderUserId, String receiverUserId, String timestamp, String senderName, String receiverName) {
        this.message = message;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.timestamp = timestamp;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
