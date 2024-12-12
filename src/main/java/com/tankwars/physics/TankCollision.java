package com.tankwars.physics;

import com.tankwars.game.terrain.Terrain;
import java.util.ArrayList;
import java.util.List;

public class TankCollision {
    // Tank sprite dimensions
    public static final int TANK_WIDTH = 29;
    public static final int TANK_HEIGHT = 17;
    
    // Define tank hitbox points (in local coordinates relative to center)
    private static final Point[] TANK_HITBOX = {
        // Convert from sprite coordinates (0,0 at top-left) to center-based coordinates
        new Point(0 - 14.5, 10 - 8.5),      // (0,10)
        new Point(0 - 14.5, 12 - 8.5),      // (0,12)
        new Point(4 - 14.5, 16 - 8.5),      // (4,16)
        new Point(24 - 14.5, 16 - 8.5),     // (24,16)
        new Point(28 - 14.5, 12 - 8.5),     // (28,12)
        new Point(28 - 14.5, 10 - 8.5),     // (28,10)
        new Point(26 - 14.5, 8 - 8.5),      // (26,8)
        new Point(23 - 14.5, 8 - 8.5),      // (23,8)
        new Point(22 - 14.5, 5 - 8.5),      // (22,5)
        new Point(19 - 14.5, 0 - 8.5),      // (19,0)
        new Point(9 - 14.5, 0 - 8.5),       // (9,0)
        new Point(6 - 14.5, 5 - 8.5),       // (6,5)
        new Point(5 - 14.5, 8 - 8.5),       // (5,8)
        new Point(2 - 14.5, 8 - 8.5)        // (2,8)
    };
    
    private static class Point {
        final double x, y;
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static class ContactPoint {
        public final double x;      // World X position
        public final double y;      // World Y position
        public final double force;  // Penetration depth
        
        public ContactPoint(double x, double y, double force) {
            this.x = x;
            this.y = y;
            this.force = force;
        }
    }
    
    private static final double CONTACT_BUFFER = 2.0;  // Buffer distance in pixels
    private static final double MIN_CONTACT_DISTANCE = 5.0;  // Minimum distance between contact points
    
    public static List<ContactPoint> findContactPoints(double tankX, double tankY, 
            double tankAngle, Terrain terrain) {
        List<ContactPoint> contacts = new ArrayList<>();
        
        // Transform hitbox points to world coordinates
        double cos = Math.cos(tankAngle);
        double sin = Math.sin(tankAngle);
        
        // Check each edge of the polygon for terrain intersection
        for (int i = 0; i < TANK_HITBOX.length; i++) {
            Point p1 = TANK_HITBOX[i];
            Point p2 = TANK_HITBOX[(i + 1) % TANK_HITBOX.length];
            
            // Transform points to world space
            double x1 = tankX + (p1.x * cos - p1.y * sin);
            double y1 = tankY + (p1.x * sin + p1.y * cos);
            double x2 = tankX + (p2.x * cos - p2.y * sin);
            double y2 = tankY + (p2.x * sin + p2.y * cos);
            
            // Check terrain intersection along this edge
            checkEdgeCollision(x1, y1, x2, y2, terrain, contacts);
        }
        
        return contacts;
    }
    
    private static void checkEdgeCollision(double x1, double y1, double x2, double y2, 
            Terrain terrain, List<ContactPoint> contacts) {
        // Sample points along the edge
        int steps = (int)Math.ceil(Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1)));
        steps = Math.max(steps, 1);
        
        ContactPoint lastContact = null;
        
        for (int i = 0; i <= steps; i++) {
            double t = (double)i / steps;
            double x = x1 + (x2 - x1) * t;
            double y = y1 + (y2 - y1) * t;
            
            int terrainX = (int)x;
            if (terrainX >= 0 && terrainX < terrain.getWidth()) {
                double terrainHeight = terrain.getHeights()[terrainX];
                double bufferHeight = terrainHeight - CONTACT_BUFFER;
                
                if (y >= bufferHeight) {
                    double depth = y - terrainHeight;
                    ContactPoint newContact = new ContactPoint(x, terrainHeight, depth);
                    
                    // Only add if it's far enough from the last contact point
                    if (lastContact == null || 
                        distance(lastContact.x, lastContact.y, x, terrainHeight) >= MIN_CONTACT_DISTANCE) {
                        contacts.add(newContact);
                        lastContact = newContact;
                    }
                }
            }
        }
    }
    
    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
} 