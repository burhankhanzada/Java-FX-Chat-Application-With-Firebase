package com.burhankhanzada.java.project.models;

public class User {

    public String uid;
    public String username;
    public String password;
    public String imageUrl;
    public boolean isOnline;

    public User() {
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

}
