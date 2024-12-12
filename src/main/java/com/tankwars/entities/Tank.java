package com.tankwars.entities;

import javafx.scene.image.Image;
import com.tankwars.utils.SpriteLoader;

public class Tank {
    private double x;
    private double y;
    private double terrainAngle;
    private double barrelAngle;
    private double velocity;  // Horizontal velocity
    private final Image bodySprite;
    private final Image barrelSprite;
    
    private static final double MOVE_SPEED = 0.1;
    private static final double MAX_SPEED = 0.25;
    private static final double FRICTION = 0.9;
    
    public Tank(double x, double y, String color) {
        this.x = x;
        this.y = y;
        this.terrainAngle = 0;
        this.barrelAngle = 0;
        this.velocity = 0;
        this.bodySprite = SpriteLoader.loadSprite("tanks/bodies/tank_body_" + color + ".png");
        this.barrelSprite = SpriteLoader.loadSprite("tanks/barrels/tank_barrel_" + color + ".png");
    }
    
    public void moveLeft() {
        velocity -= MOVE_SPEED;
        if (velocity < -MAX_SPEED) velocity = -MAX_SPEED;
    }
    
    public void moveRight() {
        velocity += MOVE_SPEED;
        if (velocity > MAX_SPEED) velocity = MAX_SPEED;
    }
    
    public void update() {
        // Apply friction
        velocity *= FRICTION;
        
        // Update position based on velocity
        x += velocity;
    }
    
    // Getters and setters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getBarrelAngle() { return barrelAngle; }
    public Image getBodySprite() { return bodySprite; }
    public Image getBarrelSprite() { return barrelSprite; }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setBarrelAngle(double barrelAngle) {
        this.barrelAngle = barrelAngle;
    }
    
    // Add terrain angle getter/setter
    public double getTerrainAngle() { return terrainAngle; }
    public void setTerrainAngle(double angle) { this.terrainAngle = angle; }
    
    public double getVelocity() { return velocity; }
    public void setVelocity(double velocity) { this.velocity = velocity; }
} 