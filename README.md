
# Angry Birds Game (Java Edition)

This project is an Angry Birds-style game created in Java using the [libGDX](https://libgdx.com/) framework. 
Developed as a part of a college assignment, this project features a static GUI representation of the game, 
complete with screen transitions and UI components, but without functional event handlers.

## Project Structure
- **Java Framework**: Built using libGDX, a cross-platform game development framework.
- **IDE**: Developed in IntelliJ IDEA with Gradle setup.

### Features
1. **Home/Menu Screen**: 
   - Includes a game poster, intro music, and a loading screen.
   - Buttons allow navigation to the game screen or exit.
2. **Level Selection Screen**: 
   - Allows players to choose from three levels (Level 1, Level 2, Level 3).
   - Each level redirects to a static Game Screen.
3. **Game Screen**: 
   - Displays the main gameplay area with a static environment (birds, blocks, etc.).
   - A pause button in the corner opens the pause menu.
4. **Pause Screen**: 
   - Includes options for resuming the game, adjusting settings, or exiting.
5. **Settings Screen**: 
   - Accessible from both the Menu and Pause Screens.
   - Contains volume and other settings options.
   
### Assets
- The `assets/` folder includes:
  - Various icons and background images, such as `play.png`, `settings.png`, `exit.png`, `menu_bg.png`, `level_bg.png`, etc.
  - A configuration file `button.json` for button styles.

## How to Run
1. Ensure Java and Gradle are installed.
2. Clone the repository.
3. Use the following commands to build and run the project:
   ```bash
   ./gradlew desktop:run
   ```

## Requirements
- **Java 8 or higher**
- **Gradle 6.8 or higher** (included with the project as a Gradle wrapper)
- **libGDX 1.10.0**

## Future Updates
In the next development phases, functionality will be added to enable gameplay mechanics, including launching birds, calculating collisions, and level transitions.

## Credits
- Game graphics and assets provided in the `assets/` folder.
- Sound effects and music sources will be credited once implemented in future phases.
- Project developed as part of an academic assignment at [IIIT Delhi](https://www.iiitd.ac.in/) by Sushant Gola and Satyam 
