package com.tankwars.ui;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import com.tankwars.game.GameManager;
import com.tankwars.ui.components.TerrainView;

public class GameScene extends Scene {
    private GameManager gameManager;
    private final TerrainView terrainView;

    public GameScene(GameManager gameManager) {
        super(new StackPane(), 800, 600);
        this.gameManager = gameManager;
        
        StackPane root = (StackPane) getRoot();
        terrainView = new TerrainView(gameManager);
        root.getChildren().add(terrainView);
    }
    
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        terrainView.setGameManager(gameManager);
    }
} 