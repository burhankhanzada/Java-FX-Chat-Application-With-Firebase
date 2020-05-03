package com.burhankhanzada.java.project.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FirebaseRealtimeDatabase {

    public static FirebaseOptions getFirebaeOptions() {

        FileInputStream serviceAccount = null;

        try {
            serviceAccount = new FileInputStream("E:\\IqraUniversity\\OOP\\IntelliJ IDEA\\Project\\src\\main\\resources\\serviceAccountKey.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = null;

        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://fir-a97fe.firebaseio.com")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return options;

    }

    public static DatabaseReference getUsers() {
        return FirebaseDatabase.getInstance().getReference("users");
    }

    public static DatabaseReference getMessages() {
        return FirebaseDatabase.getInstance().getReference("messages");
    }

    public static DatabaseReference getUserOnlineStatus(String uId) {
        return getUsers().child(uId).child("online");
    }

    public static DatabaseReference getMessagesFromUser(String currentUserId, String userId) {
        return FirebaseDatabase.getInstance().getReference("messages_between_users").child(currentUserId).child(userId);
    }

}
