# Angry Birds Game

## Overview
Angry Birds is a physics-based game developed using Java, LibGDX, and Box2D. The game features a set of finite birds that the player shoots at structures containing pigs. The goal is to destroy all pigs using the fewest birds. The game includes multiple levels, different types of birds, various materials, and the ability to save and restore game states.

This project integrates core Object-Oriented Programming (OOP) principles and uses design patterns to create an efficient, modular game.

## Features

### Game Mechanics:
- Shoot birds at structures made of materials like wood, glass, and steel.
- Different bird types with unique abilities (e.g., Blackbird for explosions).
- Multiple levels with increasing difficulty.
- Pigs require multiple hits to be destroyed.

### Physics:
- Utilizes Box2D for realistic physics simulation.
- Materials have varying resistances, affecting the destruction of structures.

### Game Flow:
- Player wins by destroying all pigs.
- Player loses if they run out of birds or time.

### Serialization:
- Save and load game states for persistent gameplay.

### GUI:
- Built with LibGDX for smooth graphics and smooth transitions between game screens.

### Testing:
- Includes JUnit tests for core functionalities like login, cart operations, and game mechanics.

## How to Run the Program

### Prerequisites:
- Java JDK 8 or above
- LibGDX and Box2D libraries set up in your IDE (e.g., IntelliJ IDEA or Eclipse)

### Running the Game:
1. Clone the repository or download the source files.
2. Open the project in your preferred IDE (e.g., IntelliJ IDEA or Eclipse).
3. Configure the project dependencies:
    - Ensure that LibGDX and Box2D libraries are correctly linked to the project.
    - If not already set up, follow the instructions on LibGDX Setup to configure dependencies.
4. Build the project to ensure all necessary libraries are included.
5. Run the game:
    - Locate and run the `Lwjgl3Launcher.java` class from the IDE.
    - The game window should launch, and you can begin playing.

### Controls:
- Mouse or touch controls: Drag and aim the birds.
- Click or tap to release the bird and hit the structures/pigs.

## Game Screens:
- **Main Menu:** Start the game, view instructions, or quit.
- **Game Screen:** Play the game, shoot birds, destroy pigs.
- **Level End Screen:** View the result (win/lose), and proceed to the next level.

## License
This project is licensed under the MIT License. Feel free to modify and share!
