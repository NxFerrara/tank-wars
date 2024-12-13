package com.tankwars;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.tankwars.ui.GameScene;
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