package com.burhankhanzada.java.project.listcells;

import com.burhankhanzada.java.project.models.User;
import com.burhankhanzada.java.project.utils.StageAndSceneUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.IOException;

public class UserCell extends ListCell<User> {

    @FXML
    private HBox hBox;

    @FXML
    private HBox avatar;

    @FXML
    private Rectangle rectangle_image;

    @FXML
    private Circle circle_image;

    @FXML
    private Rectangle rectangle_online;

    @FXML
    private Circle circle_online;

    @FXML
    private Label label;

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty || item != null) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user_list_cell.fxml"));
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Shape shape_image;

            if (StageAndSceneUtil.isChatNowTheme) {
                shape_image = circle_image;
            } else {
                shape_image = rectangle_image;
            }

            if (item.imageUrl != null) {

                Image image = new Image(item.imageUrl, true);

                image.progressProperty().addListener((observable, oldValue, newValue) -> {
                    if (Math.rint(newValue.doubleValue() * 100) == 100) {
                        shape_image.setFill(new ImagePattern(image));
                        avatar.setVisible(false);
                        shape_image.setVisible(true);
                    }
                });

            } else {
                shape_image.setVisible(false);
                avatar.setVisible(true);
            }

            Shape shape_online;

            if (StageAndSceneUtil.isChatNowTheme) {
                shape_online = circle_online;
            } else {
                shape_online = rectangle_online;
            }

            if (item.isOnline) {
                shape_online.setVisible(true);
            } else {
                shape_online.setVisible(false);
            }

            String name = item.username.substring(0, 1).toUpperCase() + item.username.substring(1);

            label.setText(name);

            setText(null);

            setGraphic(hBox);
        }
    }
}
