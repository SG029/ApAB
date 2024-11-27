package com.angrybird.levels;

import com.angrybird.entities.Block;
import com.angrybird.entities.Pig;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class LevelManager {
    public static void loadLevel(int levelNumber, World world, Array<Block> blocks, Array<Pig> pigs) {
        // Clear existing blocks and pigs
        blocks.clear();
        pigs.clear();

        // Level ground reference
        float groundY = 1.3f;

        switch (levelNumber) {
            case 1:
                // Level 1 Configuration
                // Ground level blocks on the right side of the screen
                blocks.add(new Block(world, 10, groundY));  // Ground block
                blocks.add(new Block(world, 11, groundY));  // Ground block
                blocks.add(new Block(world, 12, groundY));  // Ground block

                // Blocks placed on top of the ground (1 unit above)
                blocks.add(new Block(world, 10, groundY + 1));
                blocks.add(new Block(world, 11, groundY + 1));
                blocks.add(new Block(world, 12, groundY + 1));

                // Pig placement on top of the blocks
                pigs.add(new Pig(world, 10.5f, groundY + 2)); // Center pig
                pigs.add(new Pig(world, 11.5f, groundY + 2)); // Right pig
                pigs.add(new Pig(world, 9.5f, groundY + 2));  // Left pig
                break;

            case 2:
                // Level 2 Configuration
                // Ground level blocks
                blocks.add(new Block(world, 10, groundY));
                blocks.add(new Block(world, 11, groundY));
                blocks.add(new Block(world, 12, groundY));

                // Stacked blocks forming a stepped structure
                blocks.add(new Block(world, 10, groundY + 1));
                blocks.add(new Block(world, 11, groundY + 1));
                blocks.add(new Block(world, 12, groundY + 1));

                blocks.add(new Block(world, 11, groundY + 2)); // Stepped block
                blocks.add(new Block(world, 12, groundY + 2)); // Stepped block

                // Pig placement above the blocks
                pigs.add(new Pig(world, 10.5f, groundY + 1.5f)); // First pig
                pigs.add(new Pig(world, 11.5f, groundY + 2.5f)); // Second pig, higher position
                break;

            case 3:
                // Level 3 Configuration
                // More complex structure with layers and platforms
                blocks.add(new Block(world, 10, groundY));
                blocks.add(new Block(world, 11, groundY));
                blocks.add(new Block(world, 12, groundY));
                blocks.add(new Block(world, 13, groundY));

                blocks.add(new Block(world, 10, groundY + 1));
                blocks.add(new Block(world, 11, groundY + 1));
                blocks.add(new Block(world, 12, groundY + 1));
                blocks.add(new Block(world, 13, groundY + 1));

                blocks.add(new Block(world, 10, groundY + 2));
                blocks.add(new Block(world, 11, groundY + 2));
                blocks.add(new Block(world, 12, groundY + 2));
                blocks.add(new Block(world, 13, groundY + 2));

                blocks.add(new Block(world, 11, groundY + 3)); // Top block for more height
                blocks.add(new Block(world, 12, groundY + 3)); // Top block

                blocks.add(new Block(world, 12, groundY + 4)); // Topmost block for extra challenge

                // Pig placement in different positions
                pigs.add(new Pig(world, 10.5f, groundY + 1.5f)); // Lower pig
                pigs.add(new Pig(world, 11.5f, groundY + 2.5f)); // Mid-height pig
                pigs.add(new Pig(world, 12.5f, groundY + 2.5f)); // Mid-height pig, to the right
                pigs.add(new Pig(world, 11.5f, groundY + 4.5f)); // Top pig
                pigs.add(new Pig(world, 12.5f, groundY + 4.5f)); // Top pig, to the right
                break;

            default:
                // Default level or error handling
                System.out.println("Invalid level number: " + levelNumber);
                break;
        }
    }
}
