package com.burhankhanzada.java.project.controllers;

import com.burhankhanzada.java.project.models.User;
import com.burhankhanzada.java.project.utils.FirebaseRealtimeDatabase;
import com.burhankhanzada.java.project.utils.StageAndSceneUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public static User currentUser;

    @FXML
    private ImageView image_logo;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private VBox vbox_login;

    @FXML
    private TextField textField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ListView<String> listView_groupMembers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        changeLogo();

        listView_groupMembers.setItems(FXCollections.observableArrayList(
                "Burhan Ahmed Khanzada - 46402", "Alizain - 46253", "Uzair Afatb - 45735"));

    }

    public void close() {
        System.exit(0);
    }

    private boolean validateUserNameAndPassword() {

        boolean isPassConditions = false;

        String alertText = null;

        if (textField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            alertText = "Username field name is empty";
        } else if (passwordField.getText().isEmpty() && !textField.getText().isEmpty()) {
            alertText = "Password field is empty";
        } else if (passwordField.getText().isEmpty() && textField.getText().isEmpty()) {
            alertText = "Username & Password both fields empty";
        } else {
            isPassConditions = true;
        }

        if (!isPassConditions) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(alertText);
            alert.show();
        }

        return isPassConditions;

    }

    private void matchUserNameAndPasswordWithDatabase() {

        if (validateUserNameAndPassword()) {

            vbox_login.setVisible(false);

            progressIndicator.setVisible(true);

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        String username = textField.getText();
                        String password = passwordField.getText();

                        boolean isMatched = false;

                        for (DataSnapshot snapshot_user : snapshot.getChildren()) {

                            User user = snapshot_user.getValue(User.class);

                            user.uid = snapshot_user.getKey();

                            if (user.username.equals(username) && user.password.equals(password)) {

                                isMatched = true;

                                LoginController.currentUser = user;

                                FirebaseRealtimeDatabase.getUserOnlineStatus(user.uid).setValueAsync(true);

                                Platform.runLater(StageAndSceneUtil::showMainScene);

                            }

                        }

                        FirebaseRealtimeDatabase.getUsers().removeEventListener(this);

                        if (!isMatched) {

                            Platform.runLater(() -> {

                                progressIndicator.setVisible(false);
                                vbox_login.setVisible(true);

                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("Incorrect username & password");
                                alert.show();
                            });

                        }
                    } else {
                        FirebaseRealtimeDatabase.getUsers().removeEventListener(this);
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            };

            FirebaseRealtimeDatabase.getUsers().addListenerForSingleValueEvent(valueEventListener);

        }

    }

    public void sendMethod(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    public void login() {
        matchUserNameAndPasswordWithDatabase();
    }

    public void maximize() {
        StageAndSceneUtil.toggleMaximizeScreen();
    }

    public void openSetting() {
        StageAndSceneUtil.showSettingScene();
    }

    private void changeLogo() {

        String iqraLogoPath = getClass().getResource("/images/iu-logo.png").toExternalForm();
        String chatNowLogoPath = getClass().getResource("/images/chatnow-logo.png").toExternalForm();

        Image logo;

        if (StageAndSceneUtil.isChatNowTheme) {
            logo = new Image(chatNowLogoPath);
        } else {
            logo = new Image(iqraLogoPath);
        }

        image_logo.setImage(logo);

    }

}
