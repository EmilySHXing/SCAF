package com.example.scaf.data.model;

public class User {
    private String userId;
    private String displayName;
    private String petName;
    private String password;


    public User(String userId, String displayName, String petName, String password) {
        this.userId = userId;
        this.displayName = displayName;
        this.petName = petName;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
