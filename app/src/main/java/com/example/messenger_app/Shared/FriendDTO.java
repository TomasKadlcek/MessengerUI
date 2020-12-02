package com.example.messenger_app.Shared;

public class FriendDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String friendEmail;
    private String friendUserId;

    public FriendDTO(long id, String firstName, String lastName, String friendEmail, String friendUserId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendEmail = friendEmail;
        this.friendUserId = friendUserId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }
}
