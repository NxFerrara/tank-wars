package com.tankwars.utils;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

public class SpriteLoader {
    private static final Map<String, Image> sprites = new HashMap<>();
    
    public static Image loadSprite(String path) {
        if (!sprites.containsKey(path)) {
            String resourcePath = "/sprites/" + path;
            InputStream is = SpriteLoader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("Could not find sprite: " + resourcePath);
                throw new RuntimeException("Sprite not found: " + resourcePath);
            }
            Image sprite = new Image(is);
            sprites.put(path, sprite);
        }
        return sprites.get(path);
    }
} 