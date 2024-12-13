package com.tankwars;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.tankwars.game.GameManager;
import com.tankwars.ui.MenuScene;

public class Main extends Application {
    private GameManager gameManager;

    @Override
    public void start(Stage primaryStage) {
        MenuScene menuManager = new MenuScene(gameManager);
        // Set the initial scene to the menu
        Scene menuScene = menuManager.createMenuScene(primaryStage);
        primaryStage.setTitle("Tank Wars");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}