package com.example.scaf.data.model;

public class User {
    private String userId;
    private String displayName;
    private String petName;


    public User(String userId, String displayName, String petName) {
        this.userId = userId;
        this.displayName = displayName;
        this.petName = petName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPetName() {return petName;}


}
