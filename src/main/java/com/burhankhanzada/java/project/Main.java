package com.burhankhanzada.java.project;

import com.burhankhanzada.java.project.utils.FirebaseRealtimeDatabase;
import com.burhankhanzada.java.project.utils.StageAndSceneUtil;
import com.google.firebase.FirebaseApp;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        StageAndSceneUtil.stage = stage; // this stage must be set to before calling show stage

        stage.initStyle(StageStyle.TRANSPARENT); // to remove window buttons and title

        StageAndSceneUtil.showLoginScene();

        FirebaseApp.initializeApp(FirebaseRealtimeDatabase.getFirebaeOptions());

    }

}
