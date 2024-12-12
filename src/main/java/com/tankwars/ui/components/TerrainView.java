package com.tankwars.ui.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.tankwars.game.terrain.Terrain;
import com.tankwars.game.GameManager;
import com.tankwars.entities.Tank;
import javafx.animation.AnimationTimer;

public class TerrainView extends Canvas {
    private GameManager gameManager;
    
    public TerrainView(GameManager gameManager) {
        super(800, 600);
        this.gameManager = gameManager;
        
        // Set up continuous rendering
        AnimationTimer renderer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        };
        renderer.start();
    }
    
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        
        // Draw sky
        gc.setFill(Color.SKYBLUE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw terrain
        Terrain terrain = gameManager.getTerrain();
        double[] heights = terrain.getHeights();
        
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(2);
        gc.beginPath();
        gc.moveTo(0, getHeight());
        
        for (int x = 0; x < heights.length; x++) {
            gc.lineTo(x, heights[x]);
        }
        
        gc.lineTo(getWidth(), getHeight());
        gc.closePath();
        gc.setFill(Color.GREEN);
        gc.fill();
        gc.stroke();
        
        // Draw tanks
        drawTank(gc, gameManager.getPlayer1());
        drawTank(gc, gameManager.getPlayer2());
    }
    
    private void drawTank(GraphicsContext gc, Tank tank) {
        gc.save();
        
        // Draw barrel first
        gc.save();
        gc.translate(tank.getX(), tank.getY() - 6);
        gc.rotate(Math.toDegrees(tank.getBarrelAngle()));
        gc.drawImage(tank.getBarrelSprite(), -3, -15);
        gc.restore();
        
        // Draw rotated tank body
        gc.translate(tank.getX(), tank.getY());
        gc.rotate(Math.toDegrees(tank.getTerrainAngle()));
        gc.drawImage(tank.getBodySprite(), -14.5, -8.5);
        
        gc.restore();
    }
} 