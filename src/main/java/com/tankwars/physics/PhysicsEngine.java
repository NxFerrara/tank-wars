package com.tankwars.physics;

import com.tankwars.game.terrain.Terrain;
import com.tankwars.entities.Tank;
import com.tankwars.physics.TankCollision.ContactPoint;
import java.util.List;

public class PhysicsEngine {
    private static final double GRAVITY = 0.25;
    private static final double MAX_FALL_SPEED = 5;
    private static final double TANK_MASS = 0.5;
    private static final double ANGULAR_DAMPING = 0.4;
    
    public void update(Tank tank, Terrain terrain) {
        List<ContactPoint> contacts = TankCollision.findContactPoints(
            tank.getX(), tank.getY(), tank.getTerrainAngle(), terrain);
        
        if (contacts.isEmpty()) {
            applyGravity(tank);
        } else if (contacts.size() == 1) {
            // Single point of contact - pivot around it
            handleSingleContact(tank, contacts.get(0));
        } else {
            // Multiple contact points - find stable position
            handleMultipleContacts(tank, contacts);
        }
    }
    
    private void handleSingleContact(Tank tank, ContactPoint contact) {
        // Calculate torque based on distance from center
        double dx = contact.x - tank.getX();
        double torque = TANK_MASS * GRAVITY * dx;
        
        // Apply rotation with constraints
        double currentAngle = tank.getTerrainAngle();
        double newAngle = currentAngle + (torque * ANGULAR_DAMPING);
        
        // Constrain angle to prevent flipping (-60 to +60 degrees)
        double maxAngle = Math.PI / 3.0; // 60 degrees
        newAngle = Math.max(-maxAngle, Math.min(maxAngle, newAngle));
        
        tank.setTerrainAngle(newAngle);
        
        // Keep tank at contact point
        tank.setPosition(tank.getX(), contact.y - (TankCollision.TANK_HEIGHT / 2.0));
    }
    
    private void handleMultipleContacts(Tank tank, List<ContactPoint> contacts) {
        // Find leftmost and rightmost contact points
        ContactPoint leftmost = contacts.get(0);
        ContactPoint rightmost = contacts.get(0);
        
        for (ContactPoint p : contacts) {
            if (p.x < leftmost.x) leftmost = p;
            if (p.x > rightmost.x) rightmost = p;
        }
        
        // Calculate angle based on endpoints with constraints
        double angle = Math.atan2(rightmost.y - leftmost.y, rightmost.x - leftmost.x);
        
        // Constrain angle to prevent flipping (-60 to +60 degrees)
        double maxAngle = Math.PI / 3.0; // 60 degrees
        angle = Math.max(-maxAngle, Math.min(maxAngle, angle));
        
        tank.setTerrainAngle(angle);
        
        // Position tank at average height of contact points
        double avgY = contacts.stream().mapToDouble(p -> p.y).average().getAsDouble();
        tank.setPosition(tank.getX(), avgY - (TankCollision.TANK_HEIGHT / 2.0));
    }
    
    private void applyGravity(Tank tank) {
        double newY = tank.getY() + GRAVITY;
        tank.setPosition(tank.getX(), Math.min(newY, tank.getY() + MAX_FALL_SPEED));
    }
}