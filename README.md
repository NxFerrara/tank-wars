# Tank Wars

## Overview
Tank Wars is a Java-based two-player online multiplayer game featuring physics-based tank combat on a destructible 2D map. Players control tanks and must strategically reduce their opponent's hit points to zero.

## Features 
- **Online and Local Multiplayer:** Turn-based gameplay with timer system
- **Money System:** Customize your experience by purchasing different upgrades
- **Physics Combat:** Control projectile trajectory 
- **Weapon System:** Multiple ammunition types (explosive, sniper rounds)
- **Power-ups:** Collectible items affecting movement, weapons, and health
- **Tank Movement:** Tactical repositioning across the battlefield

## Tech Stack
- JavaFX (GUI and Graphics)
- Java 17+
- Maven (Build System)
- Multithreading
- Network Programming

## Prerequisites
- JDK 17 or higher
- Apache Maven
- Any Java IDE or text editor (VS Code with Java Extension Pack recommended but optional)

## Development Setup

### 1. Installing Maven
1. Download [Apache Maven](https://maven.apache.org/download.cgi) (Binary zip archive)
2. Extract to `C:\Program Files\Apache Maven`
3. Add to System Path:
   - Open "Environment Variables"
   - Under "System Variables" → "Path"
   - Add Maven bin directory (e.g., `C:\Program Files\Apache Maven\apache-maven-3.9.9\bin`)
4. Verify: Run `mvn -version` in a new terminal

### 2. Running the Project
1. Clone the repository
2. Build: `mvn clean install`
3. Run using either:
   - Maven: `mvn javafx:run`
   - IDE: Use provided launch configuration (You can just run the Main.java file with the "Run" button)
4. To test multiplayer, open two instances of the game and connect by typing localhost in the join game field.

### Troubleshooting
- Ensure Maven is properly installed
- Run `mvn dependency:resolve` to download dependencies
- Try `mvn clean install` for a fresh build 
