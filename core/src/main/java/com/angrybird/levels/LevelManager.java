package com.angrybird.levels;

import com.angrybird.entities.Block;
import com.angrybird.entities.Pig;
import com.angrybird.entities.Triangle;
import com.angrybird.entities.Circle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class LevelManager {
    public static void loadLevel(int levelNumber, World world, Array<Block> blocks, Array<Pig> pigs, Array<Triangle> triangles, Array<Circle> circles) {
        // Clear existing blocks, pigs, triangles, and circles
        blocks.clear();
        pigs.clear();
        triangles.clear();
        circles.clear();

        // Ground reference (same for all levels)
        float groundY = 2.6f;
        float groundY_2 = 2f;

        switch (levelNumber) {
            case 1:
                // Level -1 Configuration

                // Row 1: Pig, Block, Pig
                pigs.add(new Pig(world, 10, groundY + 1));    // Pig 1
                blocks.add(new Block(world, 11, groundY + 1)); // Block 1
                pigs.add(new Pig(world, 12, groundY + 1));    // Pig 2

                // Row 2: Block, Pig, Block
                blocks.add(new Block(world, 10, groundY));    // Block 2
                pigs.add(new Pig(world, 11, groundY));        // Pig 3
                blocks.add(new Block(world, 12, groundY));    // Block 3

                // Row 3: Block, Block, Block
                blocks.add(new Block(world, 10, groundY - 1)); // Block 4
                blocks.add(new Block(world, 11, groundY - 1)); // Block 5
                blocks.add(new Block(world, 12, groundY - 1)); // Block 6

                break;

            case 2:
                // Level 2 Configuration: Pig and Block Placement as described

                // Row 1: Pig, Pig, Pig
                pigs.add(new Pig(world, 10, groundY_2 + 3));    // Pig 1
                pigs.add(new Pig(world, 11, groundY_2 + 3));    // Pig 2
                pigs.add(new Pig(world, 12, groundY_2 + 3));    // Pig 3

                // Row 2: Block, Block, Block
                blocks.add(new Block(world, 10, groundY_2+ 2));    // Block 1
                blocks.add(new Block(world, 11, groundY_2+ 2));    // Block 2
                blocks.add(new Block(world, 12, groundY_2+ 2));    // Block 3

                // Row 3: Block, Pig, Block
                blocks.add(new Block(world, 10, groundY_2 + 1)); // Block 4
                pigs.add(new Pig(world, 11, groundY_2 + 1));     // Pig 4
                blocks.add(new Block(world, 12, groundY_2 + 1)); // Block 5

                // Row 4: Block, Block, Block
                blocks.add(new Block(world, 10, groundY_2 )); // Block 6
                blocks.add(new Block(world, 11, groundY_2 )); // Block 7
                blocks.add(new Block(world, 12, groundY_2 )); // Block 8

                break;

            case 3:
                // Level 3 Configuration: Complex structure with blocks, triangles, and circles
                blocks.add(new Block(world, 10, groundY));   // Ground level block
                blocks.add(new Block(world, 11, groundY));   // Ground level block
                blocks.add(new Block(world, 12, groundY));   // Ground level block
                blocks.add(new Block(world, 13, groundY));   // Ground level block

                blocks.add(new Block(world, 10, groundY + 1));  // First level of blocks
                blocks.add(new Block(world, 11, groundY + 1));  // Second level
                blocks.add(new Block(world, 12, groundY + 1));  // Third level
                blocks.add(new Block(world, 13, groundY + 1));  // Fourth level

                // Stepped structure for added complexity
                blocks.add(new Block(world, 11, groundY + 2)); // Stepped block
                blocks.add(new Block(world, 12, groundY + 2)); // Stepped block
                blocks.add(new Block(world, 10, groundY + 3)); // Stepped block
                triangles.add(new Triangle(world, 9, groundY + 2));  // Triangle at higher level
                circles.add(new Circle(world, 12.5f, groundY + 2)); // Circle at higher level

                // Pig placements across various blocks, triangles, and circles
                pigs.add(new Pig(world, 10.5f, groundY + 1.5f)); // Lower pig (on the first stack)
                pigs.add(new Pig(world, 11.5f, groundY + 2.5f)); // Mid-height pig (on the second stack)
                pigs.add(new Pig(world, 12.5f, groundY + 2.5f)); // Mid-height pig (right side)
                pigs.add(new Pig(world, 11.5f, groundY + 3.5f)); // Top pig (on the last block)

                break;



            case 4:
                // Level 4 Configuration: Dynamic placement with a combination of blocks, triangles, and circles
                blocks.add(new Block(world, 10, groundY));    // Ground level block
                blocks.add(new Block(world, 11, groundY));    // Ground level block
                blocks.add(new Block(world, 12, groundY));    // Ground level block

                blocks.add(new Block(world, 10, groundY + 1)); // First level of blocks
                blocks.add(new Block(world, 11, groundY + 1)); // Second level
                blocks.add(new Block(world, 12, groundY + 1)); // Third level

                // More complex structure: stepping blocks higher up
                blocks.add(new Block(world, 11, groundY + 2)); // Stepped block
                blocks.add(new Block(world, 12, groundY + 2)); // Stepped block

                // Add circles and triangles
                circles.add(new Circle(world, 10.5f, groundY + 2.5f)); // Circle block at higher level
                triangles.add(new Triangle(world, 11.5f, groundY + 3)); // Triangle at higher level

                // Pig placements: More challenging positions
                pigs.add(new Pig(world, 10.5f, groundY + 1.5f)); // Lower pig (on the first stack)
                pigs.add(new Pig(world, 11.5f, groundY + 2.5f)); // Mid pig (on the stepped structure)
                pigs.add(new Pig(world, 12.5f, groundY + 2.5f)); // Mid pig (right side)
                pigs.add(new Pig(world, 10.5f, groundY + 3.5f)); // Higher pig (on the last block)
                pigs.add(new Pig(world, 11.5f, groundY + 4.5f)); // Top pig (on the last triangle)
                break;

            default:
                // Default level or error handling
                System.out.println("Invalid level number: " + levelNumber);
                break;
        }
    }
}

