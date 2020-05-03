package com.burhankhanzada.java.project.utils;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StageAndSceneUtil {

    public static Stage stage;

    private static boolean isMaximize = false;

    public static boolean isChatNowTheme = false;

    private static void showStage(Scene scene) {
        stage.setScene(scene);
        stage.centerOnScreen(); // to make stage in the center
        stage.show();
    }

    private static Scene loadScene(String fxmlFullFileNameOrPath) {

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        Scene scene = null;
        try {
            if (isMaximize) {
                scene = new Scene(FXMLLoader.load(StageAndSceneUtil.class.getResource(fxmlFullFileNameOrPath)), screenSize.getWidth(), screenSize.getHeight());
            } else {
                scene = new Scene(FXMLLoader.load(StageAndSceneUtil.class.getResource(fxmlFullFileNameOrPath)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene.setFill(Color.TRANSPARENT); // to remove white corners of scene
        changeTheme(isChatNowTheme, scene); // to change theme
        DraggabelUtil.makeSceneDraggable(stage, scene); // to scene make draggable

        return scene;
    }

    public static void showLoginScene() {
        showStage(loadScene("/fxml/login.fxml"));
    }

    public static void showSettingScene() {
        showStage(loadScene("/fxml/setting.fxml"));
    }

    public static void showMainScene() {
        showStage(loadScene("/fxml/chat.fxml"));
    }

    public static void toggleMaximizeScreen() {
        if (isMaximize) {
            isMaximize = false;
            StageAndSceneUtil.stage.setMaximized(false);
        } else {
            isMaximize = true;
            StageAndSceneUtil.stage.setMaximized(true);
        }
    }

    public static void changeTheme(boolean isChatNowTheme, Scene scene) {

        StageAndSceneUtil.isChatNowTheme = isChatNowTheme;

        String iqraCssPath = StageAndSceneUtil.class.getResource("/css/iqra.css").toExternalForm();
        String chatNowCssPath = StageAndSceneUtil.class.getResource("/css/chatnow.css").toExternalForm();

        if (isChatNowTheme) {
            scene.getStylesheets().remove(iqraCssPath);
            scene.getStylesheets().add(chatNowCssPath);
        } else {
            scene.getStylesheets().remove(chatNowCssPath);
            scene.getStylesheets().add(iqraCssPath);
        }
    }

}
