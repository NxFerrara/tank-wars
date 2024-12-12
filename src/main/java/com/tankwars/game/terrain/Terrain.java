package com.tankwars.game.terrain;

public class Terrain {
    private final double[] heights;
    private final int width;
    
    public Terrain(int width) {
        this.width = width;
        TerrainGenerator generator = new TerrainGenerator();
        this.heights = generator.generateTerrain(width);
    }
    
    public double[] getHeights() {
        return heights;
    }
    
    public int getWidth() {
        return width;
    }
} 