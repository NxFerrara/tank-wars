package com.tankwars.entities;

public class Projectile {
    private final String name;
    private final String imagePath;

    public Projectile(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
} 