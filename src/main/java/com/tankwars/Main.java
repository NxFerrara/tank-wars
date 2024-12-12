package com.tankwars;

import javafx.application.Application;
import javafx.stage.Stage;
import com.tankwars.ui.GameScene;
import com.tankwars.game.GameManager;

public class Main extends Application {
    private GameManager gameManager;

    @Override
    public void start(Stage primaryStage) {
        GameScene gameScene = new GameScene(null);
        gameManager = new GameManager(gameScene);
        gameScene.setGameManager(gameManager);
        
        primaryStage.setTitle("Tank Wars");
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}