package com.tankwars.ui;

import com.tankwars.game.GameManager;
import com.tankwars.utils.SoundManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuScene extends Scene {

    private GameManager gameManager; // Declare the GameManager

    public MenuScene(GameManager gameManager) {
        super(new StackPane(), 800, 800);
        this.gameManager = gameManager;
        
        // Initialize and load sounds
        SoundManager.loadSounds();
    }

    public Scene createMenuScene(Stage primaryStage) {
        // Start playing menu music
        SoundManager.playMusic("menu_music");
        
        Font customFont = Font.loadFont("file:src/main/resources/fonts/ITC Machine Medium.otf", 36);

        Button playButton = new Button("Play");
        Button rulesButton = new Button("Rules");
        Button quitButton = new Button("Quit");
        playButton.setFont(customFont);
        rulesButton.setFont(customFont);
        quitButton.setFont(customFont);
        String defaultButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #000000;"
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;"
                + "-fx-font-weight: bold";
        String hoverButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #1a1a1a;" // Slightly lighter black
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;"
                + "-fx-font-weight: bold;";
        String pressButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #333333;" 
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;" 
                + "-fx-font-weight: bold;";
        playButton.setStyle(defaultButtonStyle);
        rulesButton.setStyle(defaultButtonStyle);
        quitButton.setStyle(defaultButtonStyle);
        playButton.setPrefWidth(300);
        rulesButton.setPrefWidth(300);
        quitButton.setPrefWidth(300);
        playButton.setFocusTraversable(false);
        rulesButton.setFocusTraversable(false);
        quitButton.setFocusTraversable(false);

        // Add hover sound effects to buttons
        playButton.setOnMouseEntered(e -> SoundManager.playSound("button_click"));
        rulesButton.setOnMouseEntered(e -> SoundManager.playSound("button_click"));
        quitButton.setOnMouseEntered(e -> SoundManager.playSound("button_click"));
        playButton.setOnMouseEntered(e -> {
            playButton.setStyle(hoverButtonStyle);
            SoundManager.playSound("button_click");
        });
        playButton.setOnMouseExited(e -> playButton.setStyle(defaultButtonStyle));
        playButton.setOnMousePressed(e -> playButton.setStyle(pressButtonStyle));
        playButton.setOnMouseReleased(e -> playButton.setStyle(defaultButtonStyle));
        rulesButton.setOnMouseEntered(e -> {
            rulesButton.setStyle(hoverButtonStyle); 
            SoundManager.playSound("button_click");
        });
        rulesButton.setOnMouseExited(e -> rulesButton.setStyle(defaultButtonStyle));
        rulesButton.setOnMousePressed(e -> rulesButton.setStyle(pressButtonStyle));
        rulesButton.setOnMouseReleased(e -> rulesButton.setStyle(defaultButtonStyle));
        quitButton.setOnMouseEntered(e -> {
            quitButton.setStyle(hoverButtonStyle); 
            SoundManager.playSound("button_click");
        });
        quitButton.setOnMouseExited(e -> quitButton.setStyle(defaultButtonStyle)); 
        quitButton.setOnMousePressed(e -> quitButton.setStyle(pressButtonStyle));
        quitButton.setOnMouseReleased(e -> quitButton.setStyle(defaultButtonStyle));
        // Set button actions
        playButton.setOnAction(e -> startGame(primaryStage));
        rulesButton.setOnAction(e -> showRules(primaryStage));
        quitButton.setOnAction(e -> primaryStage.close());

        // Layout for buttons
        VBox layout = new VBox(40, playButton, rulesButton, quitButton);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #000000;" +
                "-fx-background-image: url('file:src/main/resources/images/background.jpeg'); " +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-size: contain;");

        return new Scene(layout, 800, 800);
    }

    private void startGame(Stage primaryStage) {
        // Stop menu music before starting game
        SoundManager.stopMusic();
        
        // Create and set up the game scene
        GameScene gameScene = new GameScene(null);
        GameManager gameManager = new GameManager(gameScene);
        gameScene.setGameManager(gameManager);

        primaryStage.setScene(gameScene);
    }

    private void showRules(Stage primaryStage) {
        // Create a rules scene
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-background-color: #1b1b1b; -fx-padding: 20;");
        Font customFont = Font.loadFont("file:src/main/resources/fonts/ITC Machine Medium.otf", 36);

        String defaultButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #000000;"
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;";
        Button backButton = new Button("Back to Menu");
        backButton.setStyle(defaultButtonStyle);
        backButton.setFont(customFont);

        backButton.setOnMouseEntered(e -> backButton.setStyle(defaultButtonStyle.replace("#4caf50", "#45a049")));
        backButton.setOnMouseExited(e -> backButton.setStyle(defaultButtonStyle));

        backButton.setOnAction(e -> primaryStage.setScene(createMenuScene(primaryStage)));

        layout.getChildren().add(backButton);

        Scene rulesScene = new Scene(layout, 800, 800);
        primaryStage.setScene(rulesScene);
    }
} 