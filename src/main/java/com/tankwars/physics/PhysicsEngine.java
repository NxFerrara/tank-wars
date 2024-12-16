package com.tankwars.physics;

import com.tankwars.game.terrain.Terrain;
import com.tankwars.entities.Tank;
import java.util.HashMap;
import java.util.Map;

public class PhysicsEngine {
    // Physics constants
    private static final double GRAVITY = 0.4;
    private static final double ACCELERATION = 0.0025;
    
    private final Map<Integer, PhysicsState> tankStates = new HashMap<>();
    
    private static class PhysicsState {
        double velocityY = 0;
        boolean isGrounded = false;
    }
    
    public void update(Tank tank, Terrain terrain) {
        PhysicsState state = tankStates.computeIfAbsent(tank.hashCode(), k -> new PhysicsState());
        
        // Apply movement forces with acceleration
        if (tank.isMovingLeft()) {
            tank.setVelocity(-Tank.MAX_SPEED);
        } else if (tank.isMovingRight()) {
            tank.setVelocity(Tank.MAX_SPEED);
        } else {
            tank.setVelocity(0);
        }
        
        // Calculate terrain interaction
        double centerX = tank.getX() + tank.getVelocity();
        double wheelBase = TankCollision.TANK_WIDTH * 0.7;
        
        // Constrain to map bounds
        centerX = Math.max(wheelBase/2, Math.min(centerX, terrain.getWidth() - wheelBase/2));
        
        double frontX = centerX + (wheelBase / 2);
        double backX = centerX - (wheelBase / 2);
        
        double centerHeight = getTerrainHeightAt((int)centerX, terrain);
        double frontHeight = getTerrainHeightAt((int)frontX, terrain);
        double backHeight = getTerrainHeightAt((int)backX, terrain);
        
        double slopeAngle = Math.atan2(frontHeight - backHeight, wheelBase);
        
        // Ground check and positioning
        double tankBottom = tank.getY() + (TankCollision.TANK_HEIGHT / 2.0);
        state.isGrounded = tankBottom >= centerHeight - 1.0;
        
        if (state.isGrounded) {
            // Snap to ground using constrained X position
            double targetY = centerHeight - (TankCollision.TANK_HEIGHT / 2.0);
            tank.setPosition(centerX, targetY);
            state.velocityY = 0;
        } else {
            // Apply gravity when in air, but still use constrained X position
            state.velocityY = Math.min(state.velocityY + GRAVITY, 8.0);
            tank.setPosition(centerX, tank.getY() + state.velocityY);
        }
        
        // Update tank angle to match terrain
        tank.setTerrainAngle(slopeAngle);
    }
    
    private double getTerrainHeightAt(int x, Terrain terrain) {
        x = Math.max(0, Math.min(x, terrain.getWidth() - 1));
        return terrain.getHeights()[x];
    }
}