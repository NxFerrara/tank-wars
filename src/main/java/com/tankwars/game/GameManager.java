package com.tankwars.game;

import com.tankwars.entities.Tank;
import com.tankwars.entities.PowerUp;
import com.tankwars.game.terrain.Terrain;
import com.tankwars.physics.PhysicsEngine;
import javafx.animation.Timeline;
import javafx.scene.Scene;

public abstract class GameManager {
    protected Terrain terrain;
    protected Tank player1;
    protected Tank player2;
    protected PowerUp powerUp;
    protected PhysicsEngine physics;
    protected boolean player1Turn;
    protected Timeline turnTimer;

    public GameManager(Scene gameScene) {
        this.player1Turn = true; // Player 1 starts
        physics = new PhysicsEngine();
    }


    public abstract void setupInput(Scene scene);
    public abstract void update();


    public Terrain getTerrain() {
        return terrain;
    }

    public Tank getPlayer1() {
        return player1;
    }

    public Tank getPlayer2() {
        return player2;
    }
    public PowerUp getPowerup(){ return powerUp;}

}
