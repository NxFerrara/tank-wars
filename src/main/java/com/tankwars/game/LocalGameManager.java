package com.tankwars.game;

import java.util.Random;

import com.tankwars.entities.PowerUp;
import com.tankwars.entities.Projectile;
import com.tankwars.entities.Tank;
import com.tankwars.game.terrain.Terrain;
import com.tankwars.ui.components.TerrainView;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.text.Font;


public class LocalGameManager extends GameManager{
    private final AnimationTimer gameLoop;
    private boolean leftPressed;
    private boolean rightPressed;
    private int timeRemaining = 120; // 120 seconds per turn
    private final Button fireButton;
    private final ComboBox<Projectile> projectileSelector;
    private final Label timerLabel;
    private final Label turnBanner; // Label to display whose turn it is
    private final TerrainView terrainView;
    private final VBox player1FuelBox;
    private final VBox player2FuelBox;
    private final Rectangle player1FuelTank;
    private final Rectangle player2FuelTank;
    private final Label player1FuelText;
    private final Label player2FuelText;
    private final int player1MaxFuel;
    private final int player2MaxFuel;
    private final VBox player1HPBox;
    private final VBox player2HPBox;
    private final Rectangle player1HPTank;
    private final Rectangle player2HPTank;
    private final Label player1HPText;
    private final Label player2HPText;
    private final int player1MaxHP;
    private final int player2MaxHP;
    private final int[] player1proj;
    private final int[] player2proj;
    private final Label notificationLabel;


    
    public LocalGameManager(Scene gameScene, int player1HP, int player2HP, 
    int player1Fuel, int player2Fuel, int[] player1proj, int[] player2proj ) {
        super(gameScene);
        StackPane root = (StackPane) gameScene.getRoot();
        root.setPrefSize(800, 600); // Example size
        root.prefWidthProperty().bind(gameScene.widthProperty());
        root.prefHeightProperty().bind(gameScene.heightProperty());
        terrain = new Terrain(800, 0);
        this.player1proj = player1proj;
        this.player2proj = player2proj;
        // Place tanks at opposite ends of the terrain
        double[] heights = terrain.getHeights();
        String[] types = {"fuel", "health", "ammo"};
        String randomType = types[new Random().nextInt(types.length)];
        double terrainHeight = terrain.getHeights()[terrain.getWidth() / 2];
        powerUp = new PowerUp(terrain.getWidth() / 2, terrainHeight - 20, 40, randomType,"file:src/main/resources/images/powerup.png"); // Example power-up
        player1 = new Tank(100, heights[100] - 20, "blue", player1HP, player1Fuel, player1proj);
        player2 = new Tank(700, heights[700] - 20, "red", player2HP, player2Fuel, player2proj);
        player1MaxFuel = player1.getFuel();
        player2MaxFuel = player2.getFuel();
        player1MaxHP = player1.gethp();
        player2MaxHP = player2.gethp();
        player1FuelTank = new Rectangle(40, 100, javafx.scene.paint.Color.YELLOW); // Player 1 fuel tank
        player1FuelText = new Label(String.valueOf(player1.getFuel()));
        player1FuelText.setStyle("-fx-text-fill: blue;");
        player1FuelText.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 16));
        Label player1FuelLabel = new Label("P1 Fuel");
        player1FuelLabel.setStyle("-fx-text-fill: blue;");
        player1FuelLabel.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 14));

        player1FuelBox = new VBox(5, player1FuelLabel, player1FuelTank, player1FuelText); // Add label, tank, and text
        player1FuelBox.setAlignment(Pos.BOTTOM_LEFT); // Align contents to the bottom-left
        StackPane.setAlignment(player1FuelBox, Pos.BOTTOM_LEFT);
        StackPane.setMargin(player1FuelBox, new Insets(10, 0, 50, 60));
        player1HPTank = new Rectangle(40, 100, javafx.scene.paint.Color.RED); // Player 1 fuel tank
        player1HPText = new Label(String.valueOf(player1.gethp()));
        player1HPText.setStyle("-fx-text-fill: blue;");
        player1HPText.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 16));
        Label player1HPLabel = new Label("P1 HP");
        player1HPLabel.setStyle("-fx-text-fill: blue;");
        player1HPLabel.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 14));

        player1HPBox = new VBox(5, player1HPLabel, player1HPTank, player1HPText); // Add label, tank, and text
        player1HPBox.setAlignment(Pos.BOTTOM_LEFT); // Align contents to the bottom-left
        StackPane.setAlignment(player1HPBox, Pos.BOTTOM_LEFT);
        StackPane.setMargin(player1HPBox, new Insets(10, 0, 50, 10));
        // Player 2 Fuel Tank (Rectangle + Number + Label)
        player2FuelTank = new Rectangle(40, 100, javafx.scene.paint.Color.YELLOW); // Start full
        player2FuelText = new Label(String.valueOf(player2.getFuel()));
        player2FuelText.setStyle("-fx-text-fill:rgb(148, 5, 5);");
        player2FuelText.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 16));
        Label player2FuelLabel = new Label("P2 Fuel");
        player2FuelLabel.setStyle("-fx-text-fill: rgb(148, 5, 5);");
        player2FuelLabel.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 14));
        player2FuelBox = new VBox(5, player2FuelLabel, player2FuelTank, player2FuelText);
        player2FuelBox.setAlignment(Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(player2FuelBox, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(player2FuelBox, new Insets(10, 60, 50, 0));
        player2HPTank = new Rectangle(40, 100, javafx.scene.paint.Color.RED); // Start full
        player2HPText = new Label(String.valueOf(player2.gethp()));
        player2HPText.setStyle("-fx-text-fill:rgb(148, 5, 5);");
        player2HPText.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 16));
        Label player2HPLabel = new Label("P2 HP");
        player2HPLabel.setStyle("-fx-text-fill: rgb(148, 5, 5);");
        player2HPLabel.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 14));
        player2HPBox = new VBox(5, player2HPLabel, player2HPTank, player2HPText);
        player2HPBox.setAlignment(Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(player2HPBox, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(player2HPBox, new Insets(10, 10, 50, 0));
        timerLabel = new Label("Time Remaining: 120 seconds");
        timerLabel.setFont(Font.loadFont("file:src/main/resources/fonts/PressStart2P-Regular.ttf", 16));
        timerLabel.setStyle("-fx-text-fill: #000000;");
        StackPane.setAlignment(timerLabel, javafx.geometry.Pos.TOP_CENTER);
        StackPane.setMargin(timerLabel, new Insets(15, 0, 0, 0)); // 10px margin from the top


        turnBanner = new Label("Player 1");
        turnBanner.setFont(Font.loadFont("file:src/main/resources/fonts/Micro5-Regular.ttf", 40));
        turnBanner.setStyle(" -fx-text-fill: #0000FF;");
        StackPane.setAlignment(turnBanner, javafx.geometry.Pos.TOP_LEFT);
        StackPane.setMargin(turnBanner, new Insets(5, 0, 0, 15)); // 10px margin from the top

        terrainView = new TerrainView(this);
        fireButton = new Button("Fire");
        fireButton.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 20));
        fireButton.setStyle(
            "-fx-text-fill: white;" +
            "-fx-background-color: #4caf50;" + 
            "-fx-border-radius: 5px;" +
            "-fx-padding: 10;" 
        );

        fireButton.setOnMouseEntered(e -> 
            fireButton.setStyle(
                "-fx-text-fill: white;" +
                "-fx-background-color: #45a049;" + 
                "-fx-border-radius: 5px;" +
                "-fx-padding: 10;" 
            )
        );
        fireButton.setOnMouseExited(e -> 
            fireButton.setStyle(
                "-fx-text-fill: white;" +
                "-fx-background-color: #4caf50;" + 
                "-fx-border-radius: 5px;" +
                "-fx-padding: 10;"
            )
        );
        fireButton.setOnAction(e -> fire());
        StackPane.setAlignment(fireButton, Pos.BOTTOM_CENTER);
        projectileSelector = new ComboBox<Projectile>();

        // Add projectile types with images
        projectileSelector.getItems().addAll(
                new Projectile("Basic", "file:src/main/resources/images/basic.png"),
                new Projectile("Big Bomb", "file:src/main/resources/images/bigbomb.png"),
                new Projectile("Sniper", "file:src/main/resources/images/sniper.png")
        );
        projectileSelector.setValue(projectileSelector.getItems().get(0)); // Default to the first item

        // Set custom ListCell for rendering each item
        projectileSelector.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Projectile item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String itemName = item.getName();
                    setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 12));
                    if(player1Turn){
                        if(itemName.equals("Basic")){
                            setText(itemName + " - \u221E");
                        }
                        else if(itemName.equals("Big Bomb")){
                            setText(itemName + " - " + player1proj[0]);
                        }
                        else {
                            setText(itemName + " - " + player1proj[1]);
                        }
                    }
                    else{
                        if(itemName.equals("Basic")){
                            setText(itemName + " - \u221E");
                        }
                        else if(itemName.equals("Big Bomb")){
                            setText(itemName + " - " + player2proj[0]);
                        }
                        else {
                            setText(itemName + " - " + player2proj[1]);
                        }

                    }
                    imageView.setImage(new Image(item.getImagePath(), 24, 24, true, true));
                    setGraphic(imageView);
                }
            }
        });

        // Also set the appearance of the selected item in the dropdown button
        projectileSelector.setButtonCell(new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Projectile item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String itemName = item.getName();
                    setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 12));
                    if(player1Turn){
                        if(itemName.equals("Basic")){
                            setText(itemName + " - \u221E");
                        }
                        else if(itemName.equals("Big Bomb")){
                            setText(itemName + " - " + player1proj[0]);
                        }
                        else {
                            setText(itemName + " - " + player1proj[1]);
                        }
                    }
                    else{
                        if(itemName.equals("Basic")){
                            setText(itemName + " - \u221E");
                        }
                        else if(itemName.equals("Big Bomb")){
                            setText(itemName + " - " + player2proj[0]);
                        }
                        else {
                            setText(itemName + " - " + player2proj[1]);
                        }

                    }
                    imageView.setImage(new Image(item.getImagePath(), 24, 24, true, true));
                    setGraphic(imageView);
                }
            }
        });    
        projectileSelector.setPrefWidth(175);
        StackPane.setAlignment(projectileSelector, Pos.BOTTOM_CENTER);
        StackPane.setMargin(projectileSelector, new Insets(0, 0, 80, 0)); 
        notificationLabel = new Label();
        notificationLabel.setFont(Font.loadFont("file:src/main/resources/fonts/ITC Machine Medium.otf", 16));
        notificationLabel.setStyle(
            "-fx-text-fill: yellow;" +
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: rgba(0, 0, 0, 0.7);" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 5px;"
        );
        notificationLabel.setVisible(false);
        StackPane.setAlignment(notificationLabel, Pos.TOP_CENTER);
        StackPane.setMargin(notificationLabel, new Insets(50, 0, 0, 0));
        root.getChildren().addAll(terrainView,player1FuelBox, player2FuelBox, 
            player1HPBox, player2HPBox, timerLabel, turnBanner, fireButton,projectileSelector, notificationLabel);
        setupInput(gameScene);
        setupTurnTimer();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
        startTurn();
    }

    private void showNotification(String message) {
        notificationLabel.setText(message);
        notificationLabel.setVisible(true);
        
        // Fade out animation
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), notificationLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> notificationLabel.setVisible(false));
        fadeOut.play();
    }
    
    public void setupInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A) {
                leftPressed = true;
                updateTankMovement();
            }
            if (e.getCode() == KeyCode.D) {
                rightPressed = true;
                updateTankMovement();
            }
            if (e.getCode() == KeyCode.SPACE){
                fire();
            }
        });
        
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.A) {
                leftPressed = false;
                updateTankMovement();
            }
            if (e.getCode() == KeyCode.D) {
                rightPressed = false;
                updateTankMovement();
            }
        });
    }
    
    private void updateTankMovement() {
        player1FuelTank.setHeight(((double)player1.getFuel()/player1MaxFuel)*100);
        player1FuelText.setText(String.valueOf(player1.getFuel())); // Update fuel number
    
        player2FuelTank.setHeight(((double)player2.getFuel()/player2MaxFuel)*100);
        player2FuelText.setText(String.valueOf(player2.getFuel())); // Update fuel number
        if (player1Turn){
            if (player1.getFuel() > 0) { // Check Player 1's fuel
                if (leftPressed && rightPressed) {
                    // Both keys pressed - stop movement
                    player1.stopLeft();
                    player1.stopRight();
                } else if (leftPressed) {
                    player1.stopRight();
                    player1.moveLeft();
                    player1.consumeFuel(); // Decrease fuel
                } else if (rightPressed) {
                    player1.stopLeft();
                    player1.moveRight();
                    player1.consumeFuel(); // Decrease fuel
                } else {
                    // No keys pressed
                    player1.stopLeft();
                    player1.stopRight();
                }
            } else {
                // No fuel, stop movement
                player1.stopLeft();
                player1.stopRight();
            }
        } else {
            if (player2.getFuel() > 0) { // Check Player 2's fuel
                if (leftPressed && rightPressed) {
                    // Both keys pressed - stop movement
                    player2.stopLeft();
                    player2.stopRight();
                } else if (leftPressed) {
                    player2.stopRight();
                    player2.moveLeft();
                    player2.consumeFuel(); // Decrease fuel
                } else if (rightPressed) {
                    player2.stopLeft();
                    player2.moveRight();
                    player2.consumeFuel(); // Decrease fuel
                } else {
                    // No keys pressed
                    player2.stopLeft();
                    player2.stopRight();
                }
            } else {
                // No fuel, stop movement
                player2.stopLeft();
                player2.stopRight();
            }
        }
        
    }
    private void fire(){
        String projFired = projectileSelector.getValue().getName();
        if(player1Turn){
            if(projFired.equals("Big Bomb")){
                if(player1proj[0] > 0){
                    player1proj[0] -=1;
                    endTurn();
                }
            }
            else if(projFired.equals("Sniper")){
                if(player1proj[1] > 1){
                    player1proj[1] -= 1;
                    endTurn();
                }
            }
            else{
                endTurn();           
            }
        }
        else{
            if(projFired.equals("Big Bomb")){
                if(player2proj[0] > 0){
                    player2proj[0] -=1;
                    endTurn();
                }
            }
            else if(projFired.equals("Sniper")){
                if(player2proj[1] > 1){
                    player2proj[1] -= 1;
                    endTurn();
                }
            }
            else{
                endTurn();           
            }
        }
    }
    
    private void setupTurnTimer() {
        turnTimer = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                timeRemaining--;
                timerLabel.setText("Time Remaining: " + timeRemaining + " seconds");

                if (timeRemaining <= 0) {
                    endTurn(); // Automatically end the turn when the timer expires
                }
            })
        );
        turnTimer.setCycleCount(Timeline.INDEFINITE); // Run indefinitely until stopped
    }
    private void startTurn() {
        // Reset timer for the current turn
        timeRemaining = 120;
        timerLabel.setText("Time Remaining: 120 seconds");
        if (player1Turn) {
            turnBanner.setText("Player 1");
            turnBanner.setStyle( "-fx-text-fill: #0000FF;");
        } else {
            turnBanner.setText("Player 2");
            turnBanner.setStyle("-fx-text-fill: #FF0000;");
        }
        // Start the turn timer
        turnTimer.play();

    }
    private void endTurn() {
        turnTimer.stop(); // Stop the timer
        projectileSelector.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Projectile item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String itemName = item.getName();
                    setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 12));
                    if(player1Turn){
                        if(itemName.equals("Basic")){
                            setText(itemName + " - \u221E");
                        }
                        else if(itemName.equals("Big Bomb")){
                            setText(itemName + " - " + player1proj[0]);
                        }
                        else {
                            setText(itemName + " - " + player1proj[1]);
                        }
                    }
                    else{
                        if(itemName.equals("Basic")){
                            setText(itemName + " - \u221E");
                        }
                        else if(itemName.equals("Big Bomb")){
                            setText(itemName + " - " + player2proj[0]);
                        }
                        else {
                            setText(itemName + " - " + player2proj[1]);
                        }

                    }
                    imageView.setImage(new Image(item.getImagePath(), 24, 24, true, true));
                    setGraphic(imageView);
                }
            }
        });

        // Also set the appearance of the selected item in the dropdown button
        projectileSelector.setButtonCell(new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Projectile item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String itemName = item.getName();
                    setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 12));
                    if(player1Turn){
                        if(itemName.equals("Basic")){
                            setText(itemName + " - \u221E");
                        }
                        else if(itemName.equals("Big Bomb")){
                            setText(itemName + " - " + player1proj[0]);
                        }
                        else {
                            setText(itemName + " - " + player1proj[1]);
                        }
                    }
                    else{
                        if(itemName.equals("Basic")){
                            setText(itemName + " - \u221E");
                        }
                        else if(itemName.equals("Big Bomb")){
                            setText(itemName + " - " + player2proj[0]);
                        }
                        else {
                            setText(itemName + " - " + player2proj[1]);
                        }

                    }
                    imageView.setImage(new Image(item.getImagePath(), 24, 24, true, true));
                    setGraphic(imageView);
                }
            }
        });    
        player1Turn = !player1Turn; // Switch turns
        startTurn(); // Start the next turn
    }

    public void update() {        
        // Apply terrain physics
        player1HPTank.setHeight(((double)player1.gethp()/player1MaxHP)*100);
        player1HPText.setText(String.valueOf(player1.gethp())); // Update fuel number
    
        player2HPTank.setHeight(((double)player2.gethp()/player2MaxHP)*100);
        player2HPText.setText(String.valueOf(player2.gethp())); // Update fuel number
        physics.update(player1, terrain);
        physics.update(player2, terrain);
        checkPowerUpCollision();
    }

    protected void checkPowerUpCollision() {
        if (powerUp != null && powerUp.isActive()) {
            if (powerUp.checkCollision(player1.getX(), player1.getY(), player1.getBodySprite().getWidth(), player1.getBodySprite().getHeight())) {
                applyPowerUpEffect(player1, powerUp);
                powerUp.collect();
            } else if (powerUp.checkCollision(player2.getX(), player2.getY(), player2.getBodySprite().getWidth(), player2.getBodySprite().getHeight())) {
                applyPowerUpEffect(player2, powerUp);
                powerUp.collect();
            }
        }
    }

    public void applyPowerUpEffect(Tank tank, PowerUp powerUp) {
        switch (powerUp.getType()) {
            case "fuel":
                tank.refuel();
                if(player1Turn){
                    showNotification("Player 1 Collected Power-Up: Refuel!"); 
                }
                else{
                    showNotification("Player 2 Collected Power-Up: Refuel!"); 
                }
                break;
            case "health":
                tank.addHealth(25);
                if(player1Turn){
                    showNotification("Player 1 Collected Power-Up: Extra HP!"); 
                }
                else{
                    showNotification("Player 2 Collected Power-Up: Extra HP!"); 
                } 
                break;
            case "ammo":
                int randomNum = new Random().nextInt(2);
                if(player1Turn){
                    player1proj[randomNum] += 3;
                }
                else{
                    player2proj[randomNum] += 3;
                }
                if(player1Turn && randomNum == 0){
                    showNotification("Player 1 Collected Power-Up: 3 Big Bombs!"); 
                }
                else if(player1Turn && randomNum ==1){
                    showNotification("Player 1 Collected Power-Up: 3 Snipers!"); 
                }
                else if(!player1Turn && randomNum == 0){
                    showNotification("Player 2 Collected Power-Up: 3 Big Bombs!"); 
                }
                else{
                    showNotification("Player 2 Collected Power-Up: 3 Snipers!"); 
                }
                break;
            default:
                break;
        }
    }


    
    public Terrain getTerrain() { return terrain; }
    public Tank getPlayer1() { return player1; }
    public Tank getPlayer2() { return player2; }
    public void manuallyEndTurn() {
        endTurn(); // Allows the user to manually end the turn
    }
} 