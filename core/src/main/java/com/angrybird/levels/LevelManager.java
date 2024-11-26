package com.angrybird.levels;

import com.angrybird.entities.Block;
import com.angrybird.entities.Pig;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class LevelManager {
    public static void loadLevel(int levelNumber, World world, Array<Block> blocks, Array<Pig> pigs) {
        blocks.clear();
        pigs.clear();

        switch (levelNumber) {
            case 1:
                // Level 1 configuration
                blocks.add(new Block(world, 5, 1));
                pigs.add(new Pig(world, 6, 1));
                break;
            case 2:
                // Level 2 configuration
                blocks.add(new Block(world, 5, 1));
                blocks.add(new Block(world, 5, 2));
                pigs.add(new Pig(world, 6, 1));
                pigs.add(new Pig(world, 7, 1));
                break;
            case 3:
                // Level 3 configuration
                blocks.add(new Block(world, 5, 1));
                blocks.add(new Block(world, 5, 2));
                blocks.add(new Block(world, 5, 3));
                pigs.add(new Pig(world, 6, 1));
                pigs.add(new Pig(world, 7, 1));
                pigs.add(new Pig(world, 8, 1));
                break;
            default:
                // Default level or error handling
                break;
        }
    }
}
