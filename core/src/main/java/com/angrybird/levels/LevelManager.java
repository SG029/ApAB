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

        switch (levelNumber) {
            case 1:
                // Level 1 Configuration
                // Ground level blocks on the right side of the screen
                blocks.add(new Block(world, 10, 1.2f));
                blocks.add(new Block(world, 11, 1.2f));
                blocks.add(new Block(world, 12, 1.2f));

                // Blocks on top of ground blocks
                blocks.add(new Block(world, 10, 2.2f));
                blocks.add(new Block(world, 11, 2.2f));
                blocks.add(new Block(world, 12, 2.2f));

                // Pig placement on top of the block structure
                pigs.add(new Pig(world, 11, 3));
                pigs.add(new Pig(world, 10.5f, 3));
                pigs.add(new Pig(world, 11.5f, 3));
                break;

            case 2:
                // Level 2 Configuration
                // Block structure
                blocks.add(new Block(world, 10, 1));
                blocks.add(new Block(world, 11, 1));
                blocks.add(new Block(world, 12, 1));

                blocks.add(new Block(world, 11, 2));
                blocks.add(new Block(world, 12, 2));

                blocks.add(new Block(world, 12, 3));

                // Pig placement
                pigs.add(new Pig(world, 11.5f, 2));
                pigs.add(new Pig(world, 12, 4));
                break;

            case 3:
                // Level 3 Configuration
                // Complex block structure
                blocks.add(new Block(world, 10, 1));
                blocks.add(new Block(world, 11, 1));
                blocks.add(new Block(world, 12, 1));
                blocks.add(new Block(world, 13, 1));

                blocks.add(new Block(world, 11, 2));
                blocks.add(new Block(world, 12, 2));
                blocks.add(new Block(world, 13, 2));

                blocks.add(new Block(world, 12, 3));

                // Pig placement
                pigs.add(new Pig(world, 11.5f, 2));
                pigs.add(new Pig(world, 12.5f, 2));
                pigs.add(new Pig(world, 12, 4));
                break;

            default:
                // Default level or error handling
                System.out.println("Invalid level number: " + levelNumber);
                break;
        }
    }
}
