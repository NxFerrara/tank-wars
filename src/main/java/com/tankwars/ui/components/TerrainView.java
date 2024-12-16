package com.tankwars.ui.components;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.tankwars.entities.PowerUp;
import com.tankwars.entities.Tank;
import com.tankwars.game.terrain.Terrain;
import com.tankwars.game.GameManager;
import javafx.animation.AnimationTimer;
import java.util.Random;
import com.tankwars.entities.FiredProjectile;
import com.tankwars.entities.Explosion;

public class TerrainView extends Canvas {
    private GameManager gameManager;
    private final SkyRenderer skyRenderer;
    private final double[] noisePattern;
    private static final Color TERRAIN_COLOR = Color.rgb(34, 139, 34); // Forest Green
    
    public TerrainView(GameManager gameManager) {
        super(800, 600);
        this.gameManager = gameManager;
        
        // Initialize sky renderer
        skyRenderer = new SkyRenderer(getWidth(), getHeight());
        
        // Generate noise pattern
        Random random = new Random();
        noisePattern = new double[800];
        for (int i = 0; i < noisePattern.length; i++) {
            noisePattern[i] = random.nextDouble() * 0.2;
        }
        
        AnimationTimer renderer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                skyRenderer.update();
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
        Terrain terrain = gameManager.getTerrain();
        
        // Clear the canvas
        gc.clearRect(0, 0, getWidth(), getHeight());
        
        // Draw sky and clouds first
        skyRenderer.render(gc);
        
        // Draw terrain
        gc.save();
        gc.beginPath();
        gc.moveTo(0, getHeight());
        
        for (int x = 0; x < terrain.getWidth(); x++) {
            gc.lineTo(x, terrain.getHeightAt(x));
        }
        gc.lineTo(getWidth(), getHeight());
        gc.closePath();
        
        gc.setFill(TERRAIN_COLOR);
        gc.fill();
        
        // Apply terrain noise
        for (int x = 0; x < terrain.getWidth(); x++) {
            double height = terrain.getHeightAt(x);
            double noiseOffset = noisePattern[x % noisePattern.length] * 5;
            gc.setGlobalAlpha(0.1);
            gc.setStroke(Color.rgb(0, 0, 0, 0.1));
            gc.strokeLine(x, height + noiseOffset, x, height);
        }
        gc.restore();
        
        // Draw tanks
        Tank player1 = gameManager.getPlayer1();
        Tank player2 = gameManager.getPlayer2();
        
        drawTank(gc, player1);
        drawTank(gc, player2);
        PowerUp powerUp = gameManager.getPowerup();
        powerUp.render(gc);
        
        // Draw active projectile if it exists
        FiredProjectile projectile = gameManager.getActiveProjectile();
        if (projectile != null) {
            if (projectile.isActive()) {
                gc.save();
                gc.translate(projectile.getX(), projectile.getY());
                gc.rotate(projectile.getAngle());
                gc.drawImage(projectile.getSprite(),
                            -projectile.getSprite().getWidth() / 2,
                            -projectile.getSprite().getHeight() / 2);
                gc.restore();
            }
            
            Explosion explosion = projectile.getExplosion();
            if (explosion != null) {
                explosion.render(gc);
            }
        }
    }
    
    private void drawTank(GraphicsContext gc, Tank tank) {
        gc.save();
        
        // Move to tank position
        gc.translate(tank.getX(), tank.getY());
        
        // Rotate for terrain angle (convert from radians to degrees)
        gc.rotate(Math.toDegrees(tank.getTerrainAngle()));
        
        // Draw barrel first (behind tank body)
        gc.save();
        // Move pivot point higher up (above tank body)
        double barrelPivotY = -tank.getBodySprite().getHeight() * 0.15;
        gc.translate(0, barrelPivotY);
        gc.rotate(tank.getBarrelAngle());
        gc.drawImage(tank.getBarrelSprite(),
                    -tank.getBarrelSprite().getWidth() / 2,
                    -tank.getBarrelSprite().getHeight());
        gc.restore();
        
        // Draw tank body on top
        gc.drawImage(tank.getBodySprite(), 
                    -tank.getBodySprite().getWidth() / 2, 
                    -tank.getBodySprite().getHeight() / 2);
        
        gc.restore();
    }
} 