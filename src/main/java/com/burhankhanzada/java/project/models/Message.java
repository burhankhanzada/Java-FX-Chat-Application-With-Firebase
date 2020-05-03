package com.burhankhanzada.java.project.models;

public class Message {

    public String uid;
    public String text;
    public String userUid;
    public String userName;

    public Message() {
    }

    public Message(String uid, String text, String userUid, String userName) {
        this.uid = uid;
        this.text = text;
        this.userUid = userUid;
        this.userName = userName;
    }

}
