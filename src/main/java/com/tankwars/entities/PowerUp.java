package com.tankwars.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class PowerUp {
    private double x; // X-coordinate of the power-up
    private double y; // Y-coordinate of the power-up
    private double size; // Size of the power-up (e.g., radius for a circular power-up)
    private String type; // Type of power-up (e.g., "fuel", "health", "ammo")
    private boolean active; // Whether the power-up is still available
    private Image image; // Custom image for the power-up

    public PowerUp(double x, double y, double size, String type,String imagePath) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
        this.image = new Image(imagePath); // Load the custom image
        this.active = true; // Initially, the power-up is available
    }

    // Draw the power-up on the terrain
    public void render(GraphicsContext gc) {
        if (active) {
            gc.drawImage(image, x - size / 2, y - size / 2, size, size); // Centered rendering
        }
    }

    // Check collision with a tank
    public boolean checkCollision(double tankX, double tankY, double tankWidth, double tankHeight) {
        return active &&
               tankX < x + size && tankX + tankWidth > x &&
               tankY < y + size && tankY + tankHeight > y;
    }

    // Mark the power-up as collected
    public void collect() {
        active = false;
    }

    // Getters for the type
    public String getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }
}
