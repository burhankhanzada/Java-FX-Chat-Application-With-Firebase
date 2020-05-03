package com.burhankhanzada.java.project.listcells;

import com.burhankhanzada.java.project.controllers.LoginController;
import com.burhankhanzada.java.project.models.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class GroupMessageCell extends ListCell<Message> {

    @FXML
    private HBox hbox;

    @FXML
    private Label label;

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null || !empty) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/message_list_cell.fxml"));
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String msg;

            if (item.userUid.equals(LoginController.currentUser.uid)) {
                msg = item.text;
                label.getStyleClass().add("chat-bubble-right");
                hbox.setAlignment(Pos.CENTER_RIGHT);
            } else {
                msg = item.userName + " : " + item.text;
                label.getStyleClass().add("chat-bubble-left");
                hbox.setAlignment(Pos.CENTER_LEFT);
            }

            label.setText(msg);

            this.setText(null);
            this.setGraphic(hbox);
        } else {
            this.setText(null);
            this.setGraphic(null);
        }

    }
}
