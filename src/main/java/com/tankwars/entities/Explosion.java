package com.tankwars.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Explosion {
    private final double x;
    private final double y;
    private double currentRadius;
    private final double maxRadius;
    private boolean active = true;
    private final double expansionSpeed;
    private static final double FADE_THRESHOLD = 0.7; // When to start shrinking
    private final int maxDamage;  // Maximum damage at center
    private boolean damageDealt = false;  // Track if damage has been dealt
    
    public Explosion(double x, double y, String projectileType) {
        this.x = x;
        this.y = y;
        
        // Set explosion properties based on projectile type
        switch(projectileType) {
            case "Big Bomb" -> {
                this.maxRadius = 90;
                this.expansionSpeed = 0.5;
                this.maxDamage = 50;
            }
            case "Sniper" -> {
                this.maxRadius = 30;
                this.expansionSpeed = 1.75;
                this.maxDamage = 100;
            }
            default -> {
                this.maxRadius = 60;
                this.expansionSpeed = 1.25;
                this.maxDamage = 25;
            }
        }
        this.currentRadius = 1;
    }
    
    public void checkTankDamage(Tank tank) {
        if (!active || damageDealt) return;
        
        // Calculate distance from explosion center to tank
        double dx = tank.getX() - x;
        double dy = tank.getY() - y;
        double distance = Math.sqrt(dx*dx + dy*dy);
        
        // If tank is within explosion radius
        if (distance <= maxRadius) {
            // Calculate damage based on distance (linear falloff)
            double damageMultiplier = 1 - (distance / maxRadius);
            int damage = (int)(maxDamage * damageMultiplier);
            tank.takeDamage(Math.max(1, damage)); // Ensure at least 1 damage
        }
    }
    
    public void update() {
        if (!active) return;
        
        if (currentRadius < maxRadius * FADE_THRESHOLD) {
            currentRadius += expansionSpeed;
        } else if (currentRadius >= maxRadius) {
            active = false;
        } else {
            currentRadius += expansionSpeed * 0.25; // Slow down expansion near the end
        }
    }
    
    public void render(GraphicsContext gc) {
        if (!active) return;
        
        // Clamp opacity between 0 and 1
        double opacity = Math.max(0, Math.min(1, 1.0 - (currentRadius / maxRadius)));
        
        // Outer glow
        gc.setFill(Color.rgb(255, 165, 0, opacity * 0.3));
        gc.fillOval(x - currentRadius, y - currentRadius, currentRadius * 2, currentRadius * 2);
        
        // Middle layer
        gc.setFill(Color.rgb(255, 140, 0, opacity * 0.6));
        gc.fillOval(x - currentRadius * 0.7, y - currentRadius * 0.7, 
                   currentRadius * 1.4, currentRadius * 1.4);
        
        // Core
        gc.setFill(Color.rgb(255, 69, 0, opacity * 0.8));
        gc.fillOval(x - currentRadius * 0.4, y - currentRadius * 0.4, 
                   currentRadius * 0.8, currentRadius * 0.8);
    }
    
    public void setDamageDealt(boolean dealt) {
        this.damageDealt = dealt;
    }
    
    public boolean isActive() {
        return active;
    }
} 