package com.burhankhanzada.java.project.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class DraggabelUtil {

    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void makeSceneDraggable(Stage stage, Scene scene) {
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
