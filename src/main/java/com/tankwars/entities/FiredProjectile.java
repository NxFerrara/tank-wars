package com.tankwars.entities;

import javafx.scene.image.Image;

public class FiredProjectile {
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double angle;
    private final double gravity;
    private final Image sprite;
    private final String type;
    private boolean active = true;
    private Explosion explosion;

    public FiredProjectile(double x, double y, double angle, double power, String type, String imagePath) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.type = type;
        this.sprite = new Image(imagePath);
        this.gravity = 0.05;
        
        double radians = Math.toRadians(angle);
        this.velocityX = Math.cos(radians) * power;
        this.velocityY = -Math.sin(radians) * power;
    }

    public void update() {
        if (active) {
            x += velocityX;
            y += velocityY;
            velocityY += gravity;
        
            double velocityAngle = 90-Math.atan2(-velocityY * 0.9, velocityX);
            angle = Math.toDegrees(velocityAngle);
        } else if (explosion != null) {
            explosion.update();
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getAngle() { return angle; }
    public Image getSprite() { return sprite; }
    public boolean isActive() { return active; }
    public void deactivate() {
        active = false;
        explosion = new Explosion(x, y, type);
    }
    public String getType() { return type; }
    public Explosion getExplosion() {
        return explosion;
    }
} 