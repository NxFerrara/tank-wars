package com.tankwars.game.terrain;

import java.util.Random;

public class TerrainGenerator {
    private final Random random = new Random();
    
    public double[] generateTerrain(int width) {
        double[] heights = new double[width];
        double height = 300; // Starting height
        
        for (int i = 0; i < width; i++) {
            // Simple random walk algorithm for natural-looking terrain
            height += (random.nextDouble() - 0.5) * 5;
            // Keep height within bounds
            height = Math.min(Math.max(height, 100), 500);
            heights[i] = height;
        }
        
        return heights;
    }
} 