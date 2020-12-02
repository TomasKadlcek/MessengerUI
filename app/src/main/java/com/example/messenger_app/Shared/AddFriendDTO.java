package com.example.messenger_app.Shared;

public class AddFriendDTO {

    private String friendEmail;

    public AddFriendDTO(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }
}
