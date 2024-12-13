package com.tankwars.game;

import com.tankwars.entities.Tank;
import com.tankwars.game.terrain.Terrain;
import com.tankwars.physics.PhysicsEngine;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class GameManager {
    private final Terrain terrain;
    private final Tank player1;
    private final Tank player2;
    private final PhysicsEngine physics;
    private final AnimationTimer gameLoop;
    private boolean leftPressed;
    private boolean rightPressed;
    
    public GameManager(Scene gameScene) {
        terrain = new Terrain(800);
        physics = new PhysicsEngine();
        
        // Place tanks at opposite ends of the terrain
        double[] heights = terrain.getHeights();
        player1 = new Tank(100, heights[100] - 20, "blue");
        player2 = new Tank(700, heights[700] - 20, "red");
        
        setupInput(gameScene);
        
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
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
    
    private void update() {
        // Apply physics to both tanks
        physics.update(player1, terrain);
        physics.update(player2, terrain);
    }
    
    public Terrain getTerrain() { return terrain; }
    public Tank getPlayer1() { return player1; }
    public Tank getPlayer2() { return player2; }
} 