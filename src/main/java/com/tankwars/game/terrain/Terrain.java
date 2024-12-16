package com.tankwars.game.terrain;

import java.util.Random;

public class Terrain {
    private double[] heights;
    private static final double GROUND_LEVEL = 300; // Default ground level
    
    public Terrain(int width, int seed) {
        heights = new double[width];
        Random random;
        if(seed != 0){
            random = new Random(seed);
        }
        else{
            random = new Random();
        }
        // Generate control points for hills
        int numControlPoints = width / 100;
        double[] controlPoints = new double[numControlPoints];
        for (int i = 0; i < numControlPoints; i++) {
            controlPoints[i] = 300 + random.nextDouble() * 100;
        }
        
        // Interpolate between control points
        for (int x = 0; x < width; x++) {
            double progress = (x * (numControlPoints - 1)) / (double)(width - 1);
            int index = (int)progress;
            double t = progress - index;
            
            if (index >= numControlPoints - 1) {
                heights[x] = controlPoints[numControlPoints - 1];
            } else {
                // Cosine interpolation
                double mu2 = (1 - Math.cos(t * Math.PI)) / 2;
                heights[x] = controlPoints[index] * (1 - mu2) + controlPoints[index + 1] * mu2;
            }
        }
    }
    
    public double[] getHeights() {
        return heights;
    }
    
    public int getWidth() {
        return heights.length;
    }
    
    public double getHeightAt(int x) {
        if (x < 0 || x >= heights.length) {
            return GROUND_LEVEL;
        }
        return heights[x];
    }
    
    public double getGroundLevel() {
        return GROUND_LEVEL;
    }
} 