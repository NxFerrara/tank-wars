package com.tankwars.ui.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.image.Image;
import com.tankwars.utils.SpriteLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkyRenderer {
    private static class Cloud {
        double x, y;
        double speed;
        double opacity;
        Image sprite;
        
        Cloud(double x, double y, double speed, double opacity, Image sprite) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.opacity = opacity;
            this.sprite = sprite;
        }
        
        void update() {
            x += speed;
        }
        
        boolean isOffScreenRight(double width) {
            return x > width + sprite.getWidth();
        }
    }
    
    private final List<Cloud> clouds = new ArrayList<>();
    private final LinearGradient skyGradient;
    private final double maxCloudHeight;
    private final Random random = new Random();
    private final Image[] cloudSprites;
    private long lastCloudSpawnTime;
    
    // Cloud control variables
    private static final long MIN_INITIAL_NUM_CLOUDS = 2;
    private static final long MAX_INITIAL_NUM_CLOUDS = 4;
    private static final long CLOUD_SPAWN_INTERVAL = 1_000_000_000L;
    private static final double CLOUD_SPAWN_CHANCE = 0.05;
    private static final double MIN_CLOUD_SPEED = 0.02;
    private static final double MAX_CLOUD_SPEED = 0.06;
    private static final double MIN_CLOUD_OPACITY = 0.75;
    private static final double MAX_CLOUD_OPACITY = 1;
    
    public SkyRenderer(double width, double height) {
        cloudSprites = new Image[3];
        for (int i = 0; i < 3; i++) {
            cloudSprites[i] = SpriteLoader.loadSprite("textures/clouds/cloud-" + (i + 1) + ".png");
        }
        
        skyGradient = new LinearGradient(
            0, 0, 0, height,
            false, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(135, 206, 235)),
            new Stop(1, Color.rgb(100, 149, 237))
        );
        
        maxCloudHeight = height * 0.25;
        lastCloudSpawnTime = System.nanoTime();
        
        // Initialize with some random clouds
        int initialClouds = random.nextInt((int) (MAX_INITIAL_NUM_CLOUDS - MIN_INITIAL_NUM_CLOUDS + 1)) + (int) MIN_INITIAL_NUM_CLOUDS;
        for (int i = 0; i < initialClouds; i++) {
            createCloud(random.nextDouble() * width); // Random position across screen
        }
    }
    
    private void createCloud() {
        Image randomCloudSprite = cloudSprites[random.nextInt(cloudSprites.length)];
        clouds.add(new Cloud(
            -randomCloudSprite.getWidth(),
            random.nextDouble() * maxCloudHeight,
            MIN_CLOUD_SPEED + random.nextDouble() * (MAX_CLOUD_SPEED - MIN_CLOUD_SPEED),
            MIN_CLOUD_OPACITY + random.nextDouble() * (MAX_CLOUD_OPACITY - MIN_CLOUD_OPACITY),
            randomCloudSprite
        ));
    }

    private void createCloud(double xPosition) {
        Image randomCloudSprite = cloudSprites[random.nextInt(cloudSprites.length)];
        clouds.add(new Cloud(
            xPosition,
            random.nextDouble() * maxCloudHeight,
            MIN_CLOUD_SPEED + random.nextDouble() * (MAX_CLOUD_SPEED - MIN_CLOUD_SPEED),
            MIN_CLOUD_OPACITY + random.nextDouble() * (MAX_CLOUD_OPACITY - MIN_CLOUD_OPACITY),
            randomCloudSprite
        ));
    }

    public void update() {
        double width = 800;
        long currentTime = System.nanoTime();
        
        if (currentTime - lastCloudSpawnTime >= CLOUD_SPAWN_INTERVAL) {
            if (random.nextDouble() < CLOUD_SPAWN_CHANCE) {
                createCloud();
            }
            lastCloudSpawnTime = currentTime;
        }
        
        // Remove clouds that are completely off screen
        clouds.removeIf(cloud -> cloud.isOffScreenRight(width));
        clouds.forEach(Cloud::update);
    }
    
    public void render(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        
        // Draw sky gradient
        gc.setFill(skyGradient);
        gc.fillRect(0, 0, width, height);
        
        // Draw clouds
        for (Cloud cloud : clouds) {
            gc.save();
            gc.setGlobalAlpha(cloud.opacity);
            gc.drawImage(cloud.sprite, cloud.x, cloud.y);
            gc.restore();
        }
    }
} 