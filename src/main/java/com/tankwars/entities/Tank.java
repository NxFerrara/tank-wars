package com.tankwars.entities;

import javafx.scene.image.Image;
import com.tankwars.utils.SpriteLoader;

public class Tank {
    // Tank capabilities - made static for global access
    public static final double MAX_SPEED = 0.5;
    
    // Tank state
    private double x;
    private double y;
    private double terrainAngle;
    private double barrelAngle;
    private double velocity;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private final Image bodySprite;
    private final Image barrelSprite;
    
    public Tank(double x, double y, String color) {
        this.x = x;
        this.y = y;
        this.terrainAngle = 0;
        this.barrelAngle = 0;
        this.velocity = 0;
        this.isMovingLeft = false;
        this.isMovingRight = false;
        this.bodySprite = SpriteLoader.loadSprite("tanks/bodies/tank_body_" + color + ".png");
        this.barrelSprite = SpriteLoader.loadSprite("tanks/barrels/tank_barrel_" + color + ".png");
    }
    
    // Movement methods
    public void moveLeft() { isMovingLeft = true; }
    public void moveRight() { isMovingRight = true; }
    public void stopLeft() { isMovingLeft = false; }
    public void stopRight() { isMovingRight = false; }
    
    // Getters and setters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getBarrelAngle() { return barrelAngle; }
    public double getTerrainAngle() { return terrainAngle; }
    public double getVelocity() { return velocity; }
    public boolean isMovingLeft() { return isMovingLeft; }
    public boolean isMovingRight() { return isMovingRight; }
    public Image getBodySprite() { return bodySprite; }
    public Image getBarrelSprite() { return barrelSprite; }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setBarrelAngle(double angle) { this.barrelAngle = angle; }
    public void setTerrainAngle(double angleRadians) { 
        this.terrainAngle = angleRadians;  // Store in radians
    }
    public void setVelocity(double velocity) { this.velocity = velocity; }
} 