package com.tankwars.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, MediaPlayer> sounds = new HashMap<>();
    private static MediaPlayer musicPlayer;
    private static double musicVolume = 0.1;
    private static double effectsVolume = 0.4;
    
    public static void loadSounds() {
        // Load menu music
        loadSound("menu_music", "/sounds/final-countdown.mp3", true);
        
        // Load sound effects
        loadSound("button_click", "/sounds/button-click-1.wav", false);
        // Add more sounds as needed
    }
    
    private static void loadSound(String name, String path, boolean isMusic) {
        try {
            Media sound = new Media(SoundManager.class.getResource(path).toExternalForm());
            MediaPlayer player = new MediaPlayer(sound);
            
            if (isMusic) {
                player.setCycleCount(MediaPlayer.INDEFINITE);
                player.setVolume(musicVolume);
                sounds.put(name, player);
            } else {
                player.setVolume(effectsVolume);
                sounds.put(name, player);
            }
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + path);
            e.printStackTrace();
        }
    }
    
    public static void playMusic(String name) {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
        
        MediaPlayer player = sounds.get(name);
        if (player != null) {
            player.play();
            musicPlayer = player;
        }
    }
    
    public static void playSound(String name) {
        MediaPlayer player = sounds.get(name);
        if (player != null) {
            player.stop();
            player.play();
        }
    }
    
    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }
    
    public static void setMusicVolume(double volume) {
        musicVolume = volume;
        if (musicPlayer != null) {
            musicPlayer.setVolume(volume);
        }
    }
    
    public static void setEffectsVolume(double volume) {
        effectsVolume = volume;
        sounds.forEach((name, player) -> {
            if (player != musicPlayer) {
                player.setVolume(volume);
            }
        });
    }
} 