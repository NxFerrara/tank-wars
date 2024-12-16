package com.tankwars.entities;

import javafx.scene.image.Image;
import com.tankwars.utils.SpriteLoader;

public class Tank {
    // Tank capabilities - made static for global access
    public static final double MAX_SPEED = 0.5;
    private static final double BARREL_ROTATION_SPEED = 2.0;
    
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
    private int fuel; // Fuel for the tank
    private int maxFuel;
    private int hp;
    private int maxHP;
    private int[] proj;

    
    public Tank(double x, double y, String color, int hp, int fuel, int[] proj) {
        this.x = x;
        this.y = y;
        this.terrainAngle = 0;
        this.fuel = fuel;
        this.maxFuel = fuel;
        this.hp = hp;
        this.maxHP = hp;
        this.proj = proj;
        this.barrelAngle = 0;
        this.velocity = 0;
        this.isMovingLeft = false;
        this.isMovingRight = false;
        this.bodySprite = SpriteLoader.loadSprite("tanks/bodies/tank_body_" + color + ".png");
        this.barrelSprite = SpriteLoader.loadSprite("tanks/barrels/tank_barrel_" + color + ".png");
    }
    
    // Movement methods
    public void moveLeft() { 
        if (fuel > 0) {
            isMovingLeft = true; 
        }

    }
    public void moveRight() { 
        if (fuel > 0) {
            isMovingRight = true; 
        }
    }
    public void stopLeft() { isMovingLeft = false; }
    public void stopRight() { isMovingRight = false; }

    public void consumeFuel() { fuel--; }
    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage); // Ensure hp doesn't go below 0
    }
    
    // Getters and setters
    public double getX() { return x; }
    public double getY() { return y; }
    public int gethp() { return hp; }
    public int getFuel() { return fuel; }
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
    public void refuel(){
        this.fuel = maxFuel;
    }
    public void addHealth(int hp){
        this.hp += hp;
    }
    public void setBarrelAngle(double angle) { this.barrelAngle = angle; }
    public void setTerrainAngle(double angleRadians) { 
        this.terrainAngle = angleRadians;  // Store in radians
    }
    public void setVelocity(double velocity) { this.velocity = velocity; }

    public void adjustBarrelAngle(boolean up) {
        double delta = up ? -BARREL_ROTATION_SPEED : BARREL_ROTATION_SPEED;
        // Constrain barrel angle between 0 and 180 degrees
        // Note: 0 degrees points right, 180 points left
        if (barrelAngle + delta > 90){
            barrelAngle = 90;
        }
        else if (barrelAngle + delta < -90){
            barrelAngle = -90;
        }
        else{
            barrelAngle += delta;
        }
    }

    public FiredProjectile fireProjectile(String type, String imagePath) {
        double barrelLength = barrelSprite.getHeight();
        double firingAngle = 90 - (barrelAngle + Math.toDegrees(terrainAngle));
        
        double radians = Math.toRadians(firingAngle);
        
        double barrelBaseY = y - 0.15*bodySprite.getHeight();
        double startX = x + Math.cos(radians) * barrelLength;
        double startY = barrelBaseY - Math.sin(radians) * barrelLength;
        
        double power = switch(type) {
            case "Basic" -> 4;
            case "Big Bomb" -> 3;
            case "Cluster Bomb" -> 4;
            case "Sniper" -> 5;
            default -> 5;
        };
        
        return new FiredProjectile(startX, startY, firingAngle, power, type, imagePath);
    }
} 