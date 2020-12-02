package com.example.messenger_app.Shared;

public class AddMessageDTO {

    private String message;
    private String receiverUserId;

    public AddMessageDTO(String message, String receiverUserId) {
        this.message = message;
        this.receiverUserId = receiverUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }
}
