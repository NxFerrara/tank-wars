package com.tankwars.ui;

import com.tankwars.game.GameManager;
import com.tankwars.utils.SoundManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuScene extends Scene {

    private GameManager gameManager; // Declare the GameManager
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
        
        // Initialize and load sounds
        SoundManager.loadSounds();
    }

    public Scene createMenuScene(Stage primaryStage) {
        // Start playing menu music
        SoundManager.playMusic("menu_music");
        
        Font customFont = Font.loadFont("file:src/main/resources/fonts/ITC Machine Medium.otf", 36);

        Button playButton = new Button("Play");
        Button rulesButton = new Button("Rules");
        Button quitButton = new Button("Quit");
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
        playButton.setPrefWidth(300);
        rulesButton.setPrefWidth(300);
        quitButton.setPrefWidth(300);
        playButton.setFocusTraversable(false);
        rulesButton.setFocusTraversable(false);
        quitButton.setFocusTraversable(false);

        // Add hover sound effects to buttons
        playButton.setOnMouseEntered(e -> SoundManager.playSound("button_click"));
        rulesButton.setOnMouseEntered(e -> SoundManager.playSound("button_click"));
        quitButton.setOnMouseEntered(e -> SoundManager.playSound("button_click"));
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
        // Set button actions
        playButton.setOnAction(e -> preGame(primaryStage));
        rulesButton.setOnAction(e -> showRules(primaryStage));
        quitButton.setOnAction(e -> primaryStage.close());

        // Layout for buttons
        VBox layout = new VBox(40, playButton, rulesButton, quitButton);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #000000;" +
                "-fx-background-image: url('file:src/main/resources/images/background.jpeg'); " +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-size: contain;");

        return new Scene(layout, 800, 800);
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
        upgradeBox = createUpgradeBox(primaryStage);
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

    private VBox createUpgradeBox(Stage primaryStage) {
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
        finishTurnButton.setOnAction(e -> finishTurn(primaryStage));
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

    private void finishTurn(Stage primaryStage) {
        if (isPlayer1) {
            // Switch to Player 2's turn
            isPlayer1 = false;
            titleLabel.setText("Player 2: Upgrade Your Tank!");
            moneyLabel.setText("Money: $" + player2Money);
        } else {
            // Both players are done, start the game
            startGame(primaryStage);
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


    private void startGame(Stage primaryStage) {
        // Stop menu music before starting game
        SoundManager.stopMusic();
        
        // Create and set up the game scene
        GameScene gameScene = new GameScene(null);
        GameManager gameManager = new GameManager(gameScene, player1HP, player2HP, 
            player1Fuel,player2Fuel, player1proj, player2proj);
        gameScene.setGameManager(gameManager);

        primaryStage.setScene(gameScene);
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