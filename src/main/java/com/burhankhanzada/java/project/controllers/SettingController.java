package com.burhankhanzada.java.project.controllers;

import com.burhankhanzada.java.project.utils.StageAndSceneUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    private VBox vBox;

    @FXML
    private RadioButton radioButton_iqra;

    @FXML
    private RadioButton radioButton_chatnow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup toggleGroup = new ToggleGroup();

        radioButton_iqra.setToggleGroup(toggleGroup);
        radioButton_chatnow.setToggleGroup(toggleGroup);

        if (StageAndSceneUtil.isChatNowTheme) {
            radioButton_chatnow.setSelected(true);
        } else {
            radioButton_iqra.setSelected(true);
        }

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();

            if (radioButton.getId().equals("radioButton_iqra")) {
                StageAndSceneUtil.changeTheme(false, vBox.getScene());
            } else if (radioButton.getId().equals("radioButton_chatnow")) {
                StageAndSceneUtil.changeTheme(true, vBox.getScene());
            }

        });

    }

    public void back() {
        StageAndSceneUtil.showLoginScene();
    }

    public void maximize() {
        StageAndSceneUtil.toggleMaximizeScreen();
    }

    public void close() {
        System.exit(0);
    }

}
