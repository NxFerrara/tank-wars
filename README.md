# Tank Wars

## Overview

Tank Wars is a Java-based two-player online and local multiplayer game featuring physics-based tank combat on a randomly generated 2D map. Players control tanks and must strategically reduce their opponent's hit points to zero.

## Features

- **Online Lan Multiplayer:** Turn-based gameplay with timer system
- **Money System:** Customize your experience by purchasing different upgrades
- **Physics Combat:** Control projectile trajectory
- **Weapon System:** Multiple ammunition types (explosive, sniper rounds)
- **Power-ups:** Collectible items affecting movement, weapons, and health
- **Tank Movement:** Tactical repositioning across the battlefield

## Tech Stack

- JavaFX (GUI and Graphics)
- Java 17+
- Maven (Build System)
- Network Lan Play

## Platform Compatibility

- ✅ Windows: Fully supported and tested
- ⚠️ Mac Intel: Untested
- ❌ Mac Silicon (M1/M2): Currently not supported

## Prerequisites

- JDK 17 or higher
- Apache Maven
- Any Java IDE or text editor (VS Code with Java Extension Pack recommended but optional)

## Development Setup

### 1. Installing Java

#### Windows (Recommended Platform)

1. Download JDK 17+ from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
2. Run the installer
3. Add Java to System Path:
   - Open "Environment Variables"
   - Under "System Variables" → "Path"
   - Add Java bin directory (e.g., `C:\Program Files\Java\jdk-17\bin`)
4. Verify installation:
   ```bash
   java -version
   ```

#### macOS (Experimental Support)

1. Using Homebrew:

   ```bash
   brew install --cask temurin17  # Eclipse Temurin is a free OpenJDK distribution
   ```

   Or manually:

   1. Download JDK 17+ from [Eclipse Temurin](https://adoptium.net/)
   2. Run the .pkg installer
   3. Java will be automatically added to your PATH
2. Verify installation:

   ```bash
   java -version
   ```

### 2. Installing Maven

#### Windows (Recommended Platform)

1. Download [Apache Maven](https://maven.apache.org/download.cgi) (Binary zip archive)
2. Extract to `C:\Program Files\Apache Maven`
3. Add to System Path:
   - Open "Environment Variables"
   - Under "System Variables" → "Path"
   - Add Maven bin directory (e.g., `C:\Program Files\Apache Maven\apache-maven-3.9.9\bin`)
4. Verify: Run `mvn -version` in a new terminal

#### macOS (Experimental Support)

1. Using Homebrew:

   ```bash
   brew install maven
   ```
2. Verify installation:

   ```bash
   mvn -version
   ```

### Running the Project

1. Clone the repository
2. Build the project:

   ```bash
   mvn clean install
   ```
3. Run the project:

   ```bash
   mvn javafx:run
   ```

### Online Play

To play over a local network:

1. Start the game on both computers
2. One player selects "Host Game"
3. The other player selects "Join Game" and enters either:
   - `localhost` (if playing on the same computer)
   - The host computer's IP address (if playing over LAN)

Note: Both players must be on the same local network for LAN play to work.

### Known Issues

- **Mac Silicon (M1/M2)**: The game is currently not compatible with Apple Silicon Macs. We're working on adding support in future releases.
- **Mac Intel**: While the installation steps are provided, the game is currently untested on Intel Macs. Users may encounter unexpected behavior.
- **Windows**: This is the recommended platform for running the game, with full testing and support.

### Troubleshooting

If you encounter issues:

1. Ensure you're using Java 17 or higher:
   ```bash
   java -version
   ```
2. Verify Maven installation:
   ```bash
   mvn -version
   ```
3. Try cleaning and rebuilding:
   ```bash
   mvn clean install
   ```

### IDE Setup

#### VS Code (Recommended)

1. Install the following extensions:

   - Extension Pack for Java
   - Maven for Java
   - JavaFX Support
2. The project includes VS Code launch configurations:

   - `.vscode/launch.json`: Configures the Java debugger
   - `.vscode/tasks.json`: Sets up JavaFX dependencies
3. Running in VS Code:

   - Open the project in VS Code
   - Wait for Java projects to load
   - Click the "Run and Debug" icon in the sidebar
   - Select "Launch Tank Wars" from the dropdown
   - Click the play button or press F5
4. Debugging:

   - Set breakpoints by clicking left of line numbers
   - Use F5 to start debugging
   - Use standard debug controls (Step Over, Step Into, etc.)

Note: The VS Code configuration automatically handles JavaFX module path setup and dependency copying, making it easier to run and debug the game.
