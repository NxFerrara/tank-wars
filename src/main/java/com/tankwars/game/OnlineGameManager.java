package com.tankwars.game;

import java.io.IOException;
import java.util.Random;

import com.tankwars.entities.Explosion;
import com.tankwars.entities.FiredProjectile;
import com.tankwars.entities.PowerUp;
import com.tankwars.entities.Projectile;
import com.tankwars.entities.Tank;
import com.tankwars.game.terrain.Terrain;
import com.tankwars.ui.MenuScene;
import com.tankwars.ui.components.TerrainView;
import com.tankwars.network.GameClient;
import com.tankwars.network.GameHost;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.stage.Stage;


public class OnlineGameManager extends GameManager{
    private final Object connection;
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
    private boolean myTurn = true;
    private boolean isPlayer1 = false;
    private FiredProjectile activeProjectile;
    private final Label notificationLabel;
    private final Button backButton;
    private final Scene gameScene;
    private boolean isGameEnded = false;
    private final Stage stage;

    
    public OnlineGameManager(Stage primaryStage, Scene gameScene, int player1HP, int player2HP, 
    int player1Fuel, int player2Fuel, int[] player1proj, 
    int[] player2proj, Object connection) {
        super(gameScene);
        this.connection = connection;
        this.gameScene = gameScene;
        this.stage = primaryStage;
        if (connection instanceof GameHost){
            this.isPlayer1 = true;
        }
        else{
            this.myTurn = false;
        }
        StackPane root = (StackPane) gameScene.getRoot();
        root.setPrefSize(800, 600); // Example size
        root.prefWidthProperty().bind(gameScene.widthProperty());
        root.prefHeightProperty().bind(gameScene.heightProperty());
        terrain = new Terrain(800, 100);
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
         backButton = new Button("Back to Menu");
        backButton.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 20));
        backButton.setStyle(
            "-fx-text-fill: white;" +
            "-fx-background-color: #4caf50;" + 
            "-fx-border-radius: 5px;" +
            "-fx-padding: 10;" 
        );

        backButton.setOnMouseEntered(e -> 
            backButton.setStyle(
                "-fx-text-fill: white;" +
                "-fx-background-color: #45a049;" + 
                "-fx-border-radius: 5px;" +
                "-fx-padding: 10;" 
            )
        );
        backButton.setOnMouseExited(e -> 
            backButton.setStyle(
                "-fx-text-fill: white;" +
                "-fx-background-color: #4caf50;" + 
                "-fx-border-radius: 5px;" +
                "-fx-padding: 10;"
            )
        );
        backButton.setVisible(false);
        backButton.setDisable(true);
        backButton.setOnAction(e -> {
            cleanup();
            MenuScene menuManager = new MenuScene(null);
            Scene menuScene = menuManager.createMenuScene(stage);
            stage.setScene(menuScene);
        });
        StackPane.setAlignment(backButton, Pos.CENTER);
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
                    if(isPlayer1){
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
                    if(isPlayer1){
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
        root.getChildren().addAll( terrainView,player1FuelBox, player2FuelBox, 
            player1HPBox, player2HPBox, timerLabel, turnBanner, fireButton,projectileSelector, notificationLabel, backButton);
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

    private void showNotification(String message, int duration) {
        notificationLabel.setText(message);
        notificationLabel.setVisible(true);
        
        // Fade out animation
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(duration), notificationLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> notificationLabel.setVisible(false));
        fadeOut.play();
    }
    
    public void setupInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (!myTurn) return; // Don't process inputs if it's not my turn
            
            if (e.getCode() == KeyCode.A) {
                leftPressed = true;
                updateTankMovement();
            }
            if (e.getCode() == KeyCode.D) {
                rightPressed = true;
                updateTankMovement();
            }
            if (e.getCode() == KeyCode.Q) {
                if (isPlayer1) {
                    player1.adjustBarrelAngle(true);
                } else {
                    player2.adjustBarrelAngle(true);
                }
            }
            if (e.getCode() == KeyCode.E) {
                if (isPlayer1) {
                    player1.adjustBarrelAngle(false);
                } else {
                    player2.adjustBarrelAngle(false);
                }
            }
            if (e.getCode() == KeyCode.SPACE) {
                fire();
            }
        });
        
        scene.setOnKeyReleased(e -> {
            if (!myTurn) return;
            
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
    private void processMessage(String message) {
        if (message == null || message.isEmpty()) return;
        
        if (message.startsWith("HP_UPDATE:")) {
            String[] parts = message.split(":");
            if (parts.length < 3) return;  // Expect HP_UPDATE:player1HP:player2HP
            
            try {
                int p1HP = Integer.parseInt(parts[1]);
                int p2HP = Integer.parseInt(parts[2]);
                Platform.runLater(() -> {
                    player1.sethp(p1HP);
                    player2.sethp(p2HP);
                    // Update HP visuals
                    player1HPTank.setHeight(((double)player1.gethp()/player1MaxHP)*100);
                    player1HPText.setText(String.valueOf(player1.gethp()));
                    player2HPTank.setHeight(((double)player2.gethp()/player2MaxHP)*100);
                    player2HPText.setText(String.valueOf(player2.gethp()));
                });
            } catch (NumberFormatException e) {
                System.err.println("Error parsing HP update: " + e.getMessage());
            }
        } else if (message.startsWith("END_TURN:")) {
            String[] parts = message.split(":");
            if (parts.length < 6) {
                System.err.println("Invalid message format received: " + message);
                return;
            }
            
            try {
                double opponentX = Double.parseDouble(parts[1]);
                double opponentY = Double.parseDouble(parts[2]);
                int opponentFuel = Integer.parseInt(parts[3]);
                int myHP = Integer.parseInt(parts[4]);
                String isActive = parts[5];
                double barrelAngle = Double.parseDouble(parts[6]);

                Platform.runLater(() -> {
                    if (!isPlayer1) {
                        player1.setPosition(opponentX, opponentY);
                        player1.setFuel(opponentFuel);
                        player1.setBarrelAngle(barrelAngle);
                        player2.sethp(myHP);
                    } else {
                        player2.setPosition(opponentX, opponentY);
                        player2.setFuel(opponentFuel);
                        player2.setBarrelAngle(barrelAngle);
                        player1.sethp(myHP);
                    }
                    
                    renderTankUpdates();
                    
                    if (isActive.equals("false")) {
                        powerUp.collect();
                    }
                    
                    myTurn = true;
                    startTurn();
                    turnBanner.setText(isPlayer1 ? "Player 1's Turn" : "Player 2's Turn");
                });
            } catch (NumberFormatException e) {
                System.err.println("Error parsing message values: " + e.getMessage());
            }
        } else if (message.contains("Wins!")) {
            Platform.runLater(() -> {
                gameLoop.stop();
                showNotification(message, 100);
                fireButton.setDisable(true);
                backButton.setVisible(true);
                backButton.setDisable(false);
            });
        }

    }

    private void renderTankUpdates() {
        // Update Player 1 visuals
        player1FuelTank.setHeight(((double) player1.getFuel() / player1MaxFuel) * 100);
        player1FuelText.setText(String.valueOf(player1.getFuel()));
        // Update Player 2 visuals
        player2FuelTank.setHeight(((double) player2.getFuel() / player2MaxFuel) * 100);
        player2FuelText.setText(String.valueOf(player2.getFuel()));
        player1HPTank.setHeight(((double) player1.gethp() / player1MaxHP) * 100);
        player1HPText.setText(String.valueOf(player1.gethp()));
        // Update Player 2 visuals
        player2HPTank.setHeight(((double) player2.gethp() / player2MaxHP) * 100);
        player2HPText.setText(String.valueOf(player2.gethp()));
    }
    
    private void updateTankMovement() {
        player1FuelTank.setHeight(((double)player1.getFuel()/player1MaxFuel)*100);
        player1FuelText.setText(String.valueOf(player1.getFuel())); // Update fuel number
    
        player2FuelTank.setHeight(((double)player2.getFuel()/player2MaxFuel)*100);
        player2FuelText.setText(String.valueOf(player2.getFuel())); // Update fuel number
        if (isPlayer1){
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
    
    protected void fire() {
        if (!myTurn || activeProjectile != null) return;
        
        Tank currentTank = isPlayer1 ? player1 : player2;
        Projectile selectedProjectile = projectileSelector.getSelectionModel().getSelectedItem();
        
        if (selectedProjectile != null) {
            String projType = selectedProjectile.getName();
            
            // Check ammo and consume it
            if (isPlayer1) {
                if (projType.equals("Big Bomb") && player1proj[0] <= 0) return;
                if (projType.equals("Sniper") && player1proj[1] <= 0) return;
                
                if (projType.equals("Big Bomb")) player1proj[0]--;
                else if (projType.equals("Sniper")) player1proj[1]--;
            } else {
                if (projType.equals("Big Bomb") && player2proj[0] <= 0) return;
                if (projType.equals("Sniper") && player2proj[1] <= 0) return;
                
                if (projType.equals("Big Bomb")) player2proj[0]--;
                else if (projType.equals("Sniper")) player2proj[1]--;
            }
            
            // Create and fire projectile
            activeProjectile = currentTank.fireProjectile(projType, selectedProjectile.getImagePath());
            
            fireButton.setDisable(true);
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
        if ((isPlayer1 && myTurn) || (!isPlayer1 && !myTurn)) {
            turnBanner.setText("Player 1");
            turnBanner.setStyle( "-fx-text-fill: #0000FF;");
        } 
        else {
            turnBanner.setText("Player 2");
            turnBanner.setStyle("-fx-text-fill: #FF0000;");
        }
        
        // Re-enable fire button if it's my turn
        if (myTurn) {
            fireButton.setDisable(false);
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
                    if(isPlayer1){
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
                    if(isPlayer1){
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
        leftPressed = false;
        rightPressed = false;
        updateTankMovement(); 
        String endTurnData;
        if (isPlayer1) {
            endTurnData = String.format("END_TURN:%f:%f:%d:%d:%s:%f",
                player1.getX(), player1.getY(), player1.getFuel(), player2.gethp(), powerUp.isActive(), player1.getBarrelAngle());
        } else {
            endTurnData = String.format("END_TURN:%f:%f:%d:%d:%s:%f",
                player2.getX(), player2.getY(), player2.getFuel(), player1.gethp(), powerUp.isActive(), player2.getBarrelAngle());
        }

        try {
            if (connection instanceof GameHost) {
                ((GameHost) connection).sendMessage(endTurnData);
            } else if (connection instanceof GameClient) {
                ((GameClient) connection).sendMessage(endTurnData);
            }
            myTurn = !myTurn; // Set turn to false immediately after sending
            Platform.runLater(() -> {
                turnBanner.setText(isPlayer1 ? "Player 2's Turn" : "Player 1's Turn");
            });
        } catch (Exception e) {
            System.err.println("Error sending end turn data: " + e.getMessage());
        }
    }
    
    public void update() {
        // Always update visuals and physics regardless of turn
        renderTankUpdates();
        
        player1HPTank.setHeight(((double)player1.gethp()/player1MaxHP)*100);
        player1HPText.setText(String.valueOf(player1.gethp()));

        player2HPTank.setHeight(((double)player2.gethp()/player2MaxHP)*100);
        player2HPText.setText(String.valueOf(player2.gethp()));
        physics.update(player1, terrain);
        physics.update(player2, terrain);
        checkPowerUpCollision();
        
        // Only handle projectile updates during my turn
        if (activeProjectile != null) {
            if (activeProjectile.isActive()) {
                activeProjectile.update();
                
                if (activeProjectile.getY() >= terrain.getHeightAt((int)activeProjectile.getX()) ||
                    activeProjectile.getX() < 0 || activeProjectile.getX() > terrain.getWidth()) {
                    activeProjectile.deactivate();
                    Explosion explosion = activeProjectile.getExplosion();
                    
                    // Store HP before damage
                    int p1HPBefore = player1.gethp();
                    int p2HPBefore = player2.gethp();
                    
                    explosion.checkTankDamage(player1);
                    explosion.checkTankDamage(player2);
                    explosion.setDamageDealt(true);
                    
                    // If HP changed, send update
                    if (p1HPBefore != player1.gethp() || p2HPBefore != player2.gethp()) {
                        try {
                            String hpUpdateMessage = String.format("HP_UPDATE:%d:%d", player1.gethp(), player2.gethp());
                            if (connection instanceof GameHost) {
                                ((GameHost) connection).sendMessage(hpUpdateMessage);
                            } else if (connection instanceof GameClient) {
                                ((GameClient) connection).sendMessage(hpUpdateMessage);
                            }
                        } catch (Exception e) {
                            System.err.println("Error sending HP update: " + e.getMessage());
                        }
                    }
                    
                    // Update HP visuals immediately
                    Platform.runLater(() -> {
                        player1HPTank.setHeight(((double)player1.gethp()/player1MaxHP)*100);
                        player1HPText.setText(String.valueOf(player1.gethp()));
                        player2HPTank.setHeight(((double)player2.gethp()/player2MaxHP)*100);
                        player2HPText.setText(String.valueOf(player2.gethp()));
                    });
                }
            } else {
                activeProjectile.update();
                if (activeProjectile.getExplosion() != null && !activeProjectile.getExplosion().isActive()) {
                    activeProjectile = null;
                    if (myTurn) endTurn();  // Only end turn if it's my turn
                }
            }
        }

        if (myTurn) {
            // Handle win conditions and network messages only during my turn
            if (player1.gethp() <= 0) {
                gameLoop.stop();
                isGameEnded = true;
                showNotification("Player 2 Wins!", 100);
                // Send win message regardless of connection type
                try {
                    if (connection instanceof GameHost) {
                        ((GameHost) connection).sendMessage("Player 2 Wins!");
                        ((GameHost) connection).close();  // Close the connection
                    } else if (connection instanceof GameClient) {
                        ((GameClient) connection).sendMessage("Player 2 Wins!");
                        ((GameClient) connection).close();  // Close the connection
                    }
                } catch (Exception e) {
                    System.err.println("Error sending win message: " + e.getMessage());
                }
                fireButton.setDisable(true);
                backButton.setVisible(true);
                backButton.setDisable(false);
                return;
            } else if (player2.gethp() <= 0) {
                gameLoop.stop();
                isGameEnded = true;
                showNotification("Player 1 Wins!", 100);
                // Send win message regardless of connection type
                try {
                    if (connection instanceof GameHost) {
                        ((GameHost) connection).sendMessage("Player 1 Wins!");
                        ((GameHost) connection).close();  // Close the connection
                    } else if (connection instanceof GameClient) {
                        ((GameClient) connection).sendMessage("Player 1 Wins!");
                        ((GameClient) connection).close();  // Close the connection
                    }
                } catch (Exception e) {
                    System.err.println("Error sending win message: " + e.getMessage());
                }
                fireButton.setDisable(true);
                backButton.setVisible(true);
                backButton.setDisable(false);
                return;
            }
        } else {
            // Listen for opponent's end-turn data
            try {
                String message = null;
                if (connection instanceof GameHost) {
                    message = ((GameHost) connection).receiveMessageNonBlocking();
                } else if (connection instanceof GameClient) {
                    message = ((GameClient) connection).receiveMessageNonBlocking();
                }
                
                if (message != null && !message.isEmpty()) {
                    processMessage(message);
                }
            } catch (IOException e) {
                System.err.println("Error receiving message: " + e.getMessage());
                Platform.runLater(() -> {
                    showNotification("Connection error! Game ending.", 100);
                    gameLoop.stop();
                });
            }
        }
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
                if(isPlayer1){
                    showNotification("Player 1 Collected Power-Up: Refuel!", 7); 
                }
                else{
                    showNotification("Player 2 Collected Power-Up: Refuel!", 7); 
                }
                break;
            case "health":
                tank.addHealth(25);
                if(isPlayer1){
                    showNotification("Player 1 Collected Power-Up: Extra HP!", 7); 
                }
                else{
                    showNotification("Player 2 Collected Power-Up: Extra HP!", 7); 
                } 
                break;
            case "ammo":
                int randomNum = new Random().nextInt(2);
                if(isPlayer1){
                    player1proj[randomNum] += 3;
                }
                else{
                    player2proj[randomNum] += 3;
                }
                if(isPlayer1 && randomNum == 0){
                    showNotification("Player 1 Collected Power-Up: 3 Big Bombs!", 7); 
                }
                else if(isPlayer1 && randomNum ==1){
                    showNotification("Player 1 Collected Power-Up: 3 Snipers!", 7); 
                }
                else if(!isPlayer1 && randomNum == 0){
                    showNotification("Player 2 Collected Power-Up: 3 Big Bombs!", 7); 
                }
                else{
                    showNotification("Player 2 Collected Power-Up: 3 Snipers!", 7); 
                }
                break;
            default:
                break;
        }
    }


    
    public Terrain getTerrain() { return terrain; }
    public Tank getPlayer1() { return player1; }
    public Tank getPlayer2() { return player2; }
    public PowerUp getPowerup(){ return powerUp;}
    public void manuallyEndTurn() {
        endTurn(); // Allows the user to manually end the turn
    }

    @Override
    public FiredProjectile getActiveProjectile() {
        return activeProjectile;
    }

    public void cleanup() {
        if (connection != null) {
            try {
                if (connection instanceof GameHost) {
                    ((GameHost) connection).close();
                } else if (connection instanceof GameClient) {
                    ((GameClient) connection).close();
                }
            } catch (Exception e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
} 