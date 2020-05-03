package com.burhankhanzada.java.project.controllers;

import com.burhankhanzada.java.project.listcells.GroupMessageCell;
import com.burhankhanzada.java.project.listcells.PrivateMessageCell;
import com.burhankhanzada.java.project.listcells.UserCell;
import com.burhankhanzada.java.project.models.Message;
import com.burhankhanzada.java.project.models.User;
import com.burhankhanzada.java.project.utils.FirebaseRealtimeDatabase;
import com.burhankhanzada.java.project.utils.StageAndSceneUtil;
import com.google.firebase.database.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private boolean isGroupSelected = true;

    private User user;

    private List<Message> groupMessagesList = new ArrayList<>();

    private List<Message> privateMessagesList = new ArrayList<>();

    @FXML
    private ListView listView_users;

    @FXML
    private ListView listView_chat;

    @FXML
    private TextField textArea_messageBox;

    @FXML
    private Button button_send;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadUsers();

        loadGroupMessages();

        /* Added to prevent the enter from adding a new line to textArea_messageBox */
        textArea_messageBox.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                sendButtonAction();
                key.consume();
            }
        });

    }

    public void close() {
        System.exit(0);
    }

    private void loadUsers() {

        List<User> listUsers = new ArrayList<>();

        listView_users.setCellFactory(listViewUser -> new UserCell());

        ValueEventListener valueEventListener_users = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    listUsers.clear();

                    for (DataSnapshot snapshotUser : snapshot.getChildren()) {

                        User user = snapshotUser.getValue(User.class);

                        if (!user.uid.equals(LoginController.currentUser.uid)) {
                            listUsers.add(user);
                        }

                    }

                    Platform.runLater(() -> {
                        listView_users.setItems(FXCollections.observableArrayList(listUsers));
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };

        FirebaseRealtimeDatabase.getUsers().addValueEventListener(valueEventListener_users);
    }

    public void sendMethod(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            sendButtonAction();
        }
    }

    public void sendButtonAction() {

        textArea_messageBox.setDisable(true);
        button_send.setDisable(true);

        String text = textArea_messageBox.getText();

        if (!text.isEmpty()) {

            String msgUid = FirebaseRealtimeDatabase.getMessages().push().getKey();

            Message msg = new Message(msgUid, text, LoginController.currentUser.uid, LoginController.currentUser.username);

            if (isGroupSelected) {
                FirebaseRealtimeDatabase.getMessages().child(msgUid).setValue(msg, (error, ref) -> {
                    Platform.runLater(() -> {
                        textArea_messageBox.clear();
                        textArea_messageBox.setDisable(false);
                        button_send.setDisable(false);
                    });
                });
            } else {
                FirebaseRealtimeDatabase.getMessagesFromUser(LoginController.currentUser.uid, user.uid).child(msgUid).setValue(msg, (error, ref) -> {
                    FirebaseRealtimeDatabase.getMessagesFromUser(user.uid, LoginController.currentUser.uid).child(msgUid).setValue(msg, (error2, ref2) -> {
                        Platform.runLater(() -> {
                            textArea_messageBox.clear();
                            textArea_messageBox.setDisable(false);
                            button_send.setDisable(false);
                        });
                    });
                });
            }

        }
    }

    private void loadGroupMessages() {

        isGroupSelected = true;

        listView_chat.setCellFactory(messageListView -> new GroupMessageCell());

        ValueEventListener valueEventListener_groupMessages = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    groupMessagesList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Message msg = dataSnapshot.getValue(Message.class);

                        groupMessagesList.add(msg);

                    }

                    Platform.runLater(() -> {
                        listView_chat.setItems(FXCollections.observableList(groupMessagesList));
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };

        FirebaseRealtimeDatabase.getMessages().addValueEventListener(valueEventListener_groupMessages);

    }

    public void handleMouseClick() {

        isGroupSelected = false;

        User selectedUser = (User) listView_users.getSelectionModel().getSelectedItem();

        user = selectedUser;

        listView_chat.setItems(null);

        ValueEventListener valueEventListener_privateMessages = (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Platform.runLater(() -> {
                        listView_chat.setCellFactory(messageListView -> new PrivateMessageCell());
                    });

                    privateMessagesList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Message msg = dataSnapshot.getValue(Message.class);

                        privateMessagesList.add(msg);

                    }

                    Platform.runLater(() -> {
                        listView_chat.setItems(FXCollections.observableList(privateMessagesList));
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        FirebaseRealtimeDatabase.getMessagesFromUser(LoginController.currentUser.uid, user.uid).addValueEventListener(valueEventListener_privateMessages);

    }

    public void logout() {

        FirebaseRealtimeDatabase.getUserOnlineStatus(LoginController.currentUser.uid).setValueAsync(false);

        LoginController.currentUser = null;

        StageAndSceneUtil.showLoginScene();

    }

    public void clearChat() {
        if (isGroupSelected) {
            FirebaseRealtimeDatabase.getMessages().setValue(null, (error, ref) -> {
                groupMessagesList.clear();
                Platform.runLater(() -> {
                    listView_chat.setItems(null);
                });
            });
        } else {
            FirebaseRealtimeDatabase.getMessagesFromUser(LoginController.currentUser.uid, user.uid)
                    .setValue(null, (error, ref) -> {
                        privateMessagesList.clear();
                        Platform.runLater(() -> {
                            listView_chat.setItems(null);
                        });
                    });
        }
    }

    public void maximize() {
        StageAndSceneUtil.toggleMaximizeScreen();
    }

    public void showGroupMessages() {
        loadGroupMessages();
    }

}
