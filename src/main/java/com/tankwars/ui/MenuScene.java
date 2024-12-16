package com.tankwars.ui;

import java.io.IOException;

import com.tankwars.game.*;
import com.tankwars.utils.SoundManager;
import com.tankwars.network.*;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuScene extends Scene {

    private GameManager gameManager; // Declare the GameManager
    private GameClient client;
    private GameHost host;
    private boolean isPlayer1 = true; // Track whose turn it is
    private int player1Money = 100, player1Fuel = 300, player1HP = 100;
    private int player2Money = 100, player2Fuel = 300, player2HP = 100;
    private int[] player1proj = {0,0,0};
    private int[] player2proj = {0,0,0};
    private Label titleLabel = new Label("Player 1: Upgrade Your Tank!");
    private Label moneyLabel = new Label("Money: $100");
    private VBox upgradeBox;

    public MenuScene(GameManager gameManager) {
        super(new StackPane(), 800, 800);
        this.gameManager = gameManager;
        this.host = null;
        this.client =null;
        // Initialize and load sounds
        SoundManager.loadSounds();
    }

    private void cleanupConnections() {
        try {
            if (host != null) {
                host.close(); // Close host connection
                System.out.println("Host connection closed.");
            }
            if (client != null) {
                client.close(); // Close client connection
                System.out.println("Client connection closed.");
            }
        } catch (Exception e) {
            System.err.println("Error during connection cleanup: " + e.getMessage());
        }
    }

    public Scene createMenuScene(Stage primaryStage) {
        // Start playing menu music
        SoundManager.playMusic("menu_music");
        
        Font customFont = Font.loadFont("file:src/main/resources/fonts/ITC Machine Medium.otf", 36);

        Button playButton = new Button("Play Local");
        Button onlineButton = new Button("Play Online");
        Button rulesButton = new Button("Rules");
        Button quitButton = new Button("Quit");
        onlineButton.setFont(customFont);
        playButton.setFont(customFont);
        rulesButton.setFont(customFont);
        quitButton.setFont(customFont);
        String defaultButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #000000;"
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;"
                + "-fx-font-weight: bold";
        String hoverButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #1a1a1a;" // Slightly lighter black
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;"
                + "-fx-font-weight: bold;";
        String pressButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #333333;" 
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;" 
                + "-fx-font-weight: bold;";
        playButton.setStyle(defaultButtonStyle);
        rulesButton.setStyle(defaultButtonStyle);
        quitButton.setStyle(defaultButtonStyle);
        onlineButton.setStyle(defaultButtonStyle);
        playButton.setPrefWidth(300);
        rulesButton.setPrefWidth(300);
        quitButton.setPrefWidth(300);
        onlineButton.setPrefWidth(300);
        playButton.setFocusTraversable(false);
        rulesButton.setFocusTraversable(false);
        quitButton.setFocusTraversable(false);
        onlineButton.setFocusTraversable(false);

        // Add hover sound effects to buttons
        playButton.setOnMouseEntered(e -> {
            playButton.setStyle(hoverButtonStyle);
            SoundManager.playSound("button_click");
        });
        playButton.setOnMouseExited(e -> playButton.setStyle(defaultButtonStyle));
        playButton.setOnMousePressed(e -> playButton.setStyle(pressButtonStyle));
        playButton.setOnMouseReleased(e -> playButton.setStyle(defaultButtonStyle));
        rulesButton.setOnMouseEntered(e -> {
            rulesButton.setStyle(hoverButtonStyle); 
            SoundManager.playSound("button_click");
        });
        rulesButton.setOnMouseExited(e -> rulesButton.setStyle(defaultButtonStyle));
        rulesButton.setOnMousePressed(e -> rulesButton.setStyle(pressButtonStyle));
        rulesButton.setOnMouseReleased(e -> rulesButton.setStyle(defaultButtonStyle));
        quitButton.setOnMouseEntered(e -> {
            quitButton.setStyle(hoverButtonStyle); 
            SoundManager.playSound("button_click");
        });
        quitButton.setOnMouseExited(e -> quitButton.setStyle(defaultButtonStyle)); 
        quitButton.setOnMousePressed(e -> quitButton.setStyle(pressButtonStyle));
        quitButton.setOnMouseReleased(e -> quitButton.setStyle(defaultButtonStyle));
        onlineButton.setOnMouseEntered(e -> {
            onlineButton.setStyle(hoverButtonStyle);
            SoundManager.playSound("button_click");
        });
        onlineButton.setOnMouseExited(e -> onlineButton.setStyle(defaultButtonStyle));
        onlineButton.setOnMousePressed(e -> onlineButton.setStyle(pressButtonStyle));
        onlineButton.setOnMouseReleased(e -> onlineButton.setStyle(defaultButtonStyle));
        // Set button actions
        onlineButton.setOnAction(e -> onlineGame(primaryStage));
        playButton.setOnAction(e -> preGame(primaryStage));
        rulesButton.setOnAction(e -> showRules(primaryStage));
        quitButton.setOnAction(e -> primaryStage.close());

        // Layout for buttons
        VBox layout = new VBox(40, onlineButton, playButton, rulesButton, quitButton);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #000000;" +
                "-fx-background-image: url('file:src/main/resources/images/background.jpeg'); " +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-size: contain;");

        return new Scene(layout, 800, 800);
    }

    private void onlineGame(Stage primaryStage){
        SoundManager.stopMusic();
        VBox choiceBox = new VBox(20);
        choiceBox.setAlignment(Pos.CENTER);
        Font customFont = Font.loadFont("file:src/main/resources/fonts/ITC Machine Medium.otf", 36);
        Button hostButton = new Button("Host Game");
        Button joinButton = new Button("Join Game");
        hostButton.setFont(customFont);
        joinButton.setFont(customFont);
        String defaultButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #000000;"
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;"
                + "-fx-font-weight: bold";
        String hoverButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #1a1a1a;" // Slightly lighter black
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;"
                + "-fx-font-weight: bold;";
        String pressButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #333333;" 
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;" 
                + "-fx-font-weight: bold;";
        hostButton.setStyle(defaultButtonStyle);
        joinButton.setStyle(defaultButtonStyle);
        hostButton.setFocusTraversable(false);
        joinButton.setFocusTraversable(false);
        hostButton.setOnMouseEntered(e -> {
            hostButton.setStyle(hoverButtonStyle);
            SoundManager.playSound("button_click");
        });
        hostButton.setOnMouseExited(e -> hostButton.setStyle(defaultButtonStyle));
        hostButton.setOnMousePressed(e -> hostButton.setStyle(pressButtonStyle));
        hostButton.setOnMouseReleased(e -> hostButton.setStyle(defaultButtonStyle));
        joinButton.setOnMouseEntered(e -> {
            joinButton.setStyle(hoverButtonStyle);
            SoundManager.playSound("button_click");
        });
        joinButton.setOnMouseExited(e -> joinButton.setStyle(defaultButtonStyle));
        joinButton.setOnMousePressed(e -> joinButton.setStyle(pressButtonStyle));
        joinButton.setOnMouseReleased(e -> joinButton.setStyle(defaultButtonStyle));
        hostButton.setOnAction(e -> startHostGame(primaryStage));
        joinButton.setOnAction(e -> startJoinGame(primaryStage));
    
        choiceBox.getChildren().addAll(hostButton, joinButton);
        Scene scene = new Scene(choiceBox, 800, 800);
        primaryStage.setScene(scene);
    }
    private void startHostGame(Stage primaryStage) {
        VBox joinBox = new VBox(20);
        joinBox.setAlignment(Pos.CENTER);
        Font customFont = Font.loadFont("file:src/main/resources/fonts/ITC Machine Medium.otf", 36);
        Label textLabel = new Label("Server started. Waiting for connections on port 12345");
        textLabel.setFont(customFont);
        joinBox.getChildren().addAll(textLabel);
        Scene scene = new Scene(joinBox, 800, 800);
        primaryStage.setScene(scene);
        new Thread(() -> {
            try {
                GameHost host = new GameHost(12345);
                this.host = host;
                host.acceptConnection();
                Platform.runLater(() -> preGameP2P(primaryStage, true, host));
            } catch (IOException e) {
                System.err.println("Error hosting game: " + e.getMessage());
            }
        }).start();
        primaryStage.setOnCloseRequest(event -> {
            cleanupConnections();
        });
    }

    private void startJoinGame(Stage primaryStage) {
        VBox joinBox = new VBox(20);
        joinBox.setAlignment(Pos.CENTER);
        Font customFont = Font.loadFont("file:src/main/resources/fonts/ITC Machine Medium.otf", 36);
        Label ipLabel = new Label("Enter Host IP Address:");
        TextField ipField = new TextField();
        Button connectButton = new Button("Connect");
        ipLabel.setFont(customFont);
        ipField.setFont(customFont);
        connectButton.setFont(customFont);
        String defaultButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #000000;"
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;"
                + "-fx-font-weight: bold";
        String hoverButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #1a1a1a;" // Slightly lighter black
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;"
                + "-fx-font-weight: bold;";
        String pressButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #333333;" 
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;" 
                + "-fx-font-weight: bold;";
        connectButton.setStyle(defaultButtonStyle);
        connectButton.setFocusTraversable(false);
        connectButton.setOnMouseEntered(e -> {
            connectButton.setStyle(hoverButtonStyle);
            SoundManager.playSound("button_click");
        });
        connectButton.setOnMouseExited(e -> connectButton.setStyle(defaultButtonStyle));
        connectButton.setOnMousePressed(e -> connectButton.setStyle(pressButtonStyle));
        connectButton.setOnMouseReleased(e -> connectButton.setStyle(defaultButtonStyle));
        connectButton.setOnAction(e -> {
            new Thread(() -> {
                try {
                    GameClient client = new GameClient(ipField.getText(), 12345);    
                    this.client = client;
                    Platform.runLater(() -> preGameP2P(primaryStage, false, client));
                } catch (IOException ex) {
                    System.err.println("Error joining game: " + ex.getMessage());
                }
            }).start();
        });
    
        joinBox.getChildren().addAll(ipLabel, ipField, connectButton);
        Scene scene = new Scene(joinBox, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            cleanupConnections();
        });

    }

    private void preGameP2P(Stage primaryStage, boolean isHost,Object Connection) {
        SoundManager.stopMusic();
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
    
        // Update the title based on whether it's Player 1 or Player 2
        titleLabel.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 30));
        titleLabel.setStyle(
            "-fx-text-fill: #FFD700;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);" +
            "-fx-padding: 10;" +
            "-fx-border-color: #FFD700;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );
    
        upgradeBox = createUpgradeBox(primaryStage, isHost, Connection);
        root.getChildren().addAll(titleLabel, upgradeBox);
        
        Scene scene = new Scene(root, 800, 800);
        root.setStyle(
            "-fx-alignment: center; -fx-padding: 20; -fx-background-color: #000000;" +
            "-fx-background-image: url('file:src/main/resources/images/upgrade.jpg'); " +
            "-fx-background-repeat: no-repeat;" +
            "-fx-background-size: contain;"
        );
    
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tank Wars - Upgrades");
        if (isHost) {
            titleLabel.setText("Player 1: Upgrade Your Tank!");
            toggleUpgradeBox(true); // Host starts first, show upgrade box
        } else {
            titleLabel.setText("Waiting for Player 1 to finish...");
            toggleUpgradeBox(false); // Client waits for Player 1
        }
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            cleanupConnections();
        });
        if (!isHost) {
        // Wait for host's updates
            new Thread(() -> {
                try {
                    GameClient client = (GameClient) Connection;
                    String message;
                    while ((message = client.receiveMessage()) != null) {
                        handlePeerMessage(message, isHost);

                        // Notify client to start their turn
                        Platform.runLater(() -> {
                            titleLabel.setText("Player 2: Upgrade Your Tank!");
                            toggleUpgradeBox(true); // Show the upgrade box for Player 2
                        });
                        break; // Stop listening for now since it's turn-based
                    }
                } catch (IOException e) {
                    System.err.println("Error receiving messages: " + e.getMessage());
                }
            }).start();
        }
    }

    private void toggleUpgradeBox(boolean isVisible) {
        upgradeBox.setVisible(isVisible);
        upgradeBox.setDisable(!isVisible);
    }

    private String collectEndOfTurnData(boolean isHost) {
        if (isHost) {
            return "END_TURN:" +
                    player1Money + ":" +
                    player1Fuel + ":" +
                    player1HP + ":" +
                    player1proj[0] + ":" +
                    player1proj[1] + ":" +
                    player1proj[2];
        } else {
            return "END_TURN:" +
                    player2Money + ":" +
                    player2Fuel + ":" +
                    player2HP + ":" +
                    player2proj[0] + ":" +
                    player2proj[1] + ":" +
                    player2proj[2];
        }
    }

    private void sendEndOfTurnDataToPeer(String data, Object connection) {
        if (connection instanceof GameHost) {
            ((GameHost) connection).sendMessage(data);
        } else if (connection instanceof GameClient) {
            ((GameClient) connection).sendMessage(data);
        }
        else{
            System.out.println("Error sending end of turn data, connection is not host or client!");
        }
    }

    private void handlePeerMessage(String message, boolean isHost) {
        if (message.startsWith("END_TURN:")) {
            String[] data = message.split(":");
            if (isHost) {
                player2Money = Integer.parseInt(data[1]);
                player2Fuel = Integer.parseInt(data[2]);
                player2HP = Integer.parseInt(data[3]);
                player2proj[0] = Integer.parseInt(data[4]);
                player2proj[1] = Integer.parseInt(data[5]);
                player2proj[2] = Integer.parseInt(data[6]);
            } else {
                isPlayer1 = false;
                player1Money = Integer.parseInt(data[1]);
                player1Fuel = Integer.parseInt(data[2]);
                player1HP = Integer.parseInt(data[3]);
                player1proj[0] = Integer.parseInt(data[4]);
                player1proj[1] = Integer.parseInt(data[5]);
                player1proj[2] = Integer.parseInt(data[6]);
                toggleUpgradeBox(true); // Show the upgrade box for Player 2
            }
            updateMoneyLabel();
            System.out.println("Updated variables from peer's turn.");
        }
    }

    private void preGame(Stage primaryStage){
        SoundManager.stopMusic();
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        titleLabel.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 30));
        titleLabel.setStyle(
            "-fx-text-fill: #FFD700;" + /* Gold-colored text */
            "-fx-font-weight: bold;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);" + /* Darker background */
            "-fx-padding: 10;" +
            "-fx-border-color: #FFD700;" + /* Gold border */
            "-fx-border-width: 2px;" + /* Border thickness */
            "-fx-border-radius: 5;" + /* Rounded corners */
            "-fx-background-radius: 5;" /* Rounded corners for background */
        );       
        upgradeBox = createUpgradeBox(primaryStage, true, null);
        root.getChildren().addAll(titleLabel, upgradeBox);
        Scene scene = new Scene(root, 800, 800);
        root.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #000000;" +
        "-fx-background-image: url('file:src/main/resources/images/upgrade.jpg'); " +
        "-fx-background-repeat: no-repeat;" +
        "-fx-background-size: contain;");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tank Wars - Upgrades");
        primaryStage.show();
    }

    private VBox createUpgradeBox(Stage primaryStage, boolean isHost, Object connection) {
        VBox box = new VBox(20);
        String defaultStyle = "-fx-text-fill: white;"
        + "-fx-font-family: Lato;"
        + "-fx-background-color: #000000;"
        + "-fx-background-radius: 5px;"
        + "-fx-padding: 10;"
        + "-fx-font-weight: bold";
        String hoverStyle = "-fx-background-color: #4caf50; -fx-text-fill: black; -fx-font-weight: bold;" 
            + "-fx-background-radius: 5px; -fx-padding: 10; -fx-font-family: Lato;" ;
        box.setAlignment(Pos.CENTER);
        moneyLabel.setFont(Font.loadFont("file:src/main/resources/fonts/Baloo-Regular.ttf", 25));
        moneyLabel.setStyle(
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.8), 5, 0.3, 0, 0);" +
            "-fx-padding: 10;"
        );
        // Fuel Upgrade
        Button fuelButton = new Button("Upgrade Fuel (+50) - $20");
        fuelButton.setOnAction(e -> handleUpgrade("fuel", 50, isPlayer1 ? 20 : 20));
        fuelButton.setStyle(defaultStyle);
        fuelButton.setOnMouseEntered(e -> fuelButton.setStyle(hoverStyle));
        fuelButton.setOnMouseExited(e -> fuelButton.setStyle(defaultStyle));
        // HP Upgrade
        Button hpButton = new Button("Upgrade HP (+20) - $20");
        hpButton.setOnAction(e -> handleUpgrade("hp", 20, isPlayer1 ? 20 : 20));
        hpButton.setStyle(defaultStyle);
        hpButton.setOnMouseEntered(e -> hpButton.setStyle(hoverStyle));
        hpButton.setOnMouseExited(e -> hpButton.setStyle(defaultStyle));

        // Projectile Purchase
        String comboBoxStyle = "-fx-text-fill: white;"
        + "-fx-font-family: Lato;"
        + "-fx-background-color:rgb(0, 0, 0);"
        + "-fx-background-radius: 5px;"
        + "-fx-padding: 10;"
        + "-fx-font-weight: bold";
        ComboBox<String> projectileSelector = new ComboBox<>();
        projectileSelector.getItems().addAll( "Big Bomb - $20", "Cluster Bomb - $20", "Sniper - $20");
        projectileSelector.setValue("Big Bomb - $20");
        projectileSelector.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color:rgb(0, 0, 0);"); // Style the dropdown text
                }
                setOnMouseEntered(e -> setStyle("-fx-background-color: #4caf50; -fx-text-fill: black;"));
                setOnMouseExited(e -> setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-weight: bold;"));
            }
        });

        // Apply style to the selected item in the button
        projectileSelector.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color:rgb(0, 0, 0);"); // Style the button text
                }
            }
        });
        projectileSelector.setStyle(comboBoxStyle);

        Button buyProjectileButton = new Button("Buy Projectile");
        buyProjectileButton.setOnAction(e -> {
            String selectedProjectile = projectileSelector.getValue();
            handleProj(selectedProjectile, 20, 1);
        });
        buyProjectileButton.setStyle(defaultStyle);
        buyProjectileButton.setOnMouseEntered(e -> buyProjectileButton.setStyle(hoverStyle));
        buyProjectileButton.setOnMouseExited(e -> buyProjectileButton.setStyle(defaultStyle));
        // Finish Turn Button
        Button finishTurnButton = new Button("Finish Turn");
        finishTurnButton.setOnAction(e -> finishTurn(primaryStage, isHost, connection));
        finishTurnButton.setStyle(defaultStyle);
        finishTurnButton.setOnMouseEntered(e -> finishTurnButton.setStyle(hoverStyle));
        finishTurnButton.setOnMouseExited(e -> finishTurnButton.setStyle(defaultStyle));
        box.getChildren().addAll(moneyLabel, fuelButton, hpButton, projectileSelector, buyProjectileButton, finishTurnButton);
        return box;
    }

    private void updateMoneyLabel() {
        if (isPlayer1) {
            moneyLabel.setText("Money: $" + player1Money);
        } else {
            moneyLabel.setText("Money: $" + player2Money);
        }
    }

    private void finishTurn(Stage primaryStage, boolean isHost, Object Connection) {
        if (Connection != null){
            toggleUpgradeBox(false);
            if (isHost) {
                // Notify Player 2 to start upgrades
                sendEndOfTurnDataToPeer(collectEndOfTurnData(isHost), Connection);
                titleLabel.setText("Waiting for Player 2...");
                new Thread(() -> {
                    try {
                        GameHost host = (GameHost) Connection;
                        String message;
                        while ((message = host.receiveMessage()) != null) {
                            handlePeerMessage(message, isHost);
                            startGame(primaryStage, true, Connection);
                            break; // Stop listening for now since it's turn-based
                        }
                    } catch (IOException e) {
                        System.err.println("Error receiving messages: " + e.getMessage());
                    }
                }).start();
            } else {
                // Notify Player 1 that upgrades are done
                sendEndOfTurnDataToPeer(collectEndOfTurnData(isHost), Connection);
                startGame(primaryStage, true, Connection);
            }
        }
        else{
            if (isPlayer1) {
                // Switch to Player 2's turn
                isPlayer1 = false;
                titleLabel.setText("Player 2: Upgrade Your Tank!");
                moneyLabel.setText("Money: $" + player2Money);
            } else {
                // Both players are done, start the game
                startGame(primaryStage, false, Connection);
            }
        }

    }

    private void handleUpgrade(String type, int value, int cost) {
        if (isPlayer1) {
            if (player1Money >= cost) {
                player1Money -= cost;
                if (type.equals("fuel")) player1Fuel += value;
                if (type.equals("hp")) player1HP += value;
                System.out.println("Player 1 upgraded: " + type + ", Cost: $" + cost);
            } else {
                System.out.println("Player 1 doesn't have enough money.");
            }
        } else {
            if (player2Money >= cost) {
                player2Money -= cost;
                if (type.equals("fuel")) player2Fuel += value;
                if (type.equals("hp")) player2HP += value;
                System.out.println("Player 2 upgraded: " + type + ", Cost: $" + cost);
            } else {
                System.out.println("Player 2 doesn't have enough money.");
            }
        }
        updateMoneyLabel();
    }
    private void handleProj(String type, int cost, int amount){
        int hyphenIndex = type.indexOf('-');
        String projType = hyphenIndex != -1 ? type.substring(0, hyphenIndex) : type;
        if (isPlayer1) {
            if (player1Money >= cost) {
                player1Money -= cost;
                if (type.substring(0,3).equals("Big")) player1proj[0] += amount;
                if (type.substring(0,3).equals("Clu")) player1proj[1] += amount;
                if (type.substring(0,3).equals("Sni")) player1proj[2] += amount;
                System.out.println("Player 1 bought: " + amount + " " + projType+ ", Cost: $" + cost);
            } else {
                System.out.println("Player 1 doesn't have enough money.");
            }
        } else {
            if (player2Money >= cost) {
                player2Money -= cost;
                if (type.substring(0,3).equals("Big")) player2proj[0] += amount;
                if (type.substring(0,3).equals("Clu")) player2proj[1] += amount;
                if (type.substring(0,3).equals("Sni")) player2proj[2] += amount;
                System.out.println("Player 2 bought: " + amount + " " + projType+ ", Cost: $" + cost);
            } else {
                System.out.println("Player 2 doesn't have enough money.");
            }
        }
        updateMoneyLabel();
    }


    private void startGame(Stage primaryStage, boolean online, Object Connection) {
        System.out.println("Starting Game");
        Platform.runLater(() -> {
            try {
                System.out.println("In Try");
                GameScene gameScene = new GameScene(null);
                GameManager gameManager;
    
                if (online) {
                    System.out.println("In Online");
                    gameManager = new OnlineGameManager(gameScene, player1HP, player2HP, player1Fuel, player2Fuel, 
                                                        player1proj, player2proj, Connection);
                } else {
                    SoundManager.stopMusic();
                    gameManager = new LocalGameManager(gameScene, player1HP, player2HP, player1Fuel, player2Fuel, 
                                                       player1proj, player2proj);
                }
                System.out.println("After If");
                gameScene.setGameManager(gameManager);
                System.out.println("Done Setting Manager");
                primaryStage.setScene(gameScene);
                System.out.println("Done Setting Scene");
            } catch (Exception e) {
                System.err.println("Error starting game: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void showRules(Stage primaryStage) {
        // Create a rules scene
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-background-color: #1b1b1b; -fx-padding: 20;");
        Font customFont = Font.loadFont("file:src/main/resources/fonts/ITC Machine Medium.otf", 36);

        String defaultButtonStyle = "-fx-text-fill: white;"
                + "-fx-background-color: #000000;"
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 10;";
        Button backButton = new Button("Back to Menu");
        backButton.setStyle(defaultButtonStyle);
        backButton.setFont(customFont);

        backButton.setOnMouseEntered(e -> backButton.setStyle(defaultButtonStyle.replace("#4caf50", "#45a049")));
        backButton.setOnMouseExited(e -> backButton.setStyle(defaultButtonStyle));

        backButton.setOnAction(e -> primaryStage.setScene(createMenuScene(primaryStage)));

        layout.getChildren().add(backButton);

        Scene rulesScene = new Scene(layout, 800, 800);
        primaryStage.setScene(rulesScene);
    }
} 