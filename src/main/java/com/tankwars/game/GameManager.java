package com.tankwars.game;

import com.tankwars.entities.Tank;
import com.tankwars.game.terrain.Terrain;
import com.tankwars.physics.PhysicsEngine;
import com.tankwars.ui.components.TerrainView;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.text.Font;


public class GameManager {
    private final Terrain terrain;
    private final Tank player1;
    private final Tank player2;
    private final PhysicsEngine physics;
    private final AnimationTimer gameLoop;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean player1Turn = true; // Player 1 starts the game
    private int timeRemaining = 120; // 120 seconds per turn
    private Timeline turnTimer;
    private final Button endTurnButton;


    private final Label timerLabel;
    private final Label turnBanner; // Label to display whose turn it is
    private final TerrainView terrainView;


    
    public GameManager(Scene gameScene) {
        StackPane root = (StackPane) gameScene.getRoot();
        terrain = new Terrain(800);
        physics = new PhysicsEngine();
        // Place tanks at opposite ends of the terrain
        double[] heights = terrain.getHeights();
        player1 = new Tank(100, heights[100] - 20, "blue");
        player2 = new Tank(700, heights[700] - 20, "red");
        timerLabel = new Label("Time Remaining: 120 seconds");
        timerLabel.setFont(Font.loadFont("file:src/main/resources/fonts/PressStart2P-Regular.ttf", 16));
        timerLabel.setStyle("-fx-text-fill: #000000;");
        StackPane.setAlignment(timerLabel, javafx.geometry.Pos.TOP_CENTER);
        StackPane.setMargin(timerLabel, new Insets(15, 0, 0, 0)); // 10px margin from the top


        turnBanner = new Label("Player 1");
        turnBanner.setFont(Font.loadFont("file:src/main/resources/fonts/Micro5-Regular.ttf", 40));
        turnBanner.setStyle(" -fx-text-fill: #0000FF;");
        StackPane.setAlignment(turnBanner, javafx.geometry.Pos.TOP_LEFT);
        StackPane.setMargin(turnBanner, new Insets(5, 0, 0, 15)); // 10px margin from the top

        terrainView = new TerrainView(this);
        endTurnButton = new Button("End Turn");
        endTurnButton.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 20));
        endTurnButton.setStyle(
            "-fx-text-fill: white;" +
            "-fx-background-color: #4caf50;" + 
            "-fx-border-radius: 5px;" +
            "-fx-padding: 10;" 
        );

        endTurnButton.setOnMouseEntered(e -> 
            endTurnButton.setStyle(
                "-fx-text-fill: white;" +
                "-fx-background-color: #45a049;" + 
                "-fx-border-radius: 5px;" +
                "-fx-padding: 10;" 
            )
        );
        endTurnButton.setOnMouseExited(e -> 
            endTurnButton.setStyle(
                "-fx-text-fill: white;" +
                "-fx-background-color: #4caf50;" + 
                "-fx-border-radius: 5px;" +
                "-fx-padding: 10;"
            )
        );
        endTurnButton.setOnAction(e -> manuallyEndTurn());
        StackPane.setAlignment(endTurnButton, Pos.BOTTOM_CENTER);
        root.getChildren().addAll(terrainView, timerLabel, turnBanner, endTurnButton);
        setupInput(gameScene);
        setupTurnTimer();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
        startTurn();
    }
    
    private void setupInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A) {
                leftPressed = true;
                updateTankMovement();
            }
            if (e.getCode() == KeyCode.D) {
                rightPressed = true;
                updateTankMovement();
            }
        });
        
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.A) {
                leftPressed = false;
                updateTankMovement();
            }
            if (e.getCode() == KeyCode.D) {
                rightPressed = false;
                updateTankMovement();
            }
        });
    }
    
    private void updateTankMovement() {
        if (player1Turn){
            if (leftPressed && rightPressed) {
                // Both keys pressed - stop movement
                player1.stopLeft();
                player1.stopRight();
            } else if (leftPressed) {
                player1.stopRight();
                player1.moveLeft();
            } else if (rightPressed) {
                player1.stopLeft();
                player1.moveRight();
            } else {
                // No keys pressed
                player1.stopLeft();
                player1.stopRight();
            }
        }
        else{
            if (leftPressed && rightPressed) {
                // Both keys pressed - stop movement
                player2.stopLeft();
                player2.stopRight();
            } else if (leftPressed) {
                player2.stopRight();
                player2.moveLeft();
            } else if (rightPressed) {
                player2.stopLeft();
                player2.moveRight();
            } else {
                // No keys pressed
                player2.stopLeft();
                player2.stopRight();
            }
        }
        
    }
    
    private void setupTurnTimer() {
        turnTimer = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                timeRemaining--;
                timerLabel.setText("Time Remaining: " + timeRemaining + " seconds");

                if (timeRemaining <= 0) {
                    endTurn(); // Automatically end the turn when the timer expires
                }
            })
        );
        turnTimer.setCycleCount(Timeline.INDEFINITE); // Run indefinitely until stopped
    }
    private void startTurn() {
        // Reset timer for the current turn
        timeRemaining = 120;
        timerLabel.setText("Time Remaining: 120 seconds");
        if (player1Turn) {
            turnBanner.setText("Player 1");
            turnBanner.setStyle( "-fx-text-fill: #0000FF;");
        } else {
            turnBanner.setText("Player 2");
            turnBanner.setStyle("-fx-text-fill: #FF0000;");
        }
        // Start the turn timer
        turnTimer.play();

    }
    private void endTurn() {
        turnTimer.stop(); // Stop the timer
        player1Turn = !player1Turn; // Switch turns
        startTurn(); // Start the next turn
    }
    private void update() {        
        // Apply terrain physics
        physics.update(player1, terrain);
        physics.update(player2, terrain);
    }


    
    public Terrain getTerrain() { return terrain; }
    public Tank getPlayer1() { return player1; }
    public Tank getPlayer2() { return player2; }
    public void manuallyEndTurn() {
        endTurn(); // Allows the user to manually end the turn
    }
} 