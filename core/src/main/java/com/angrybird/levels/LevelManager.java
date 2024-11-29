package com.angrybird.levels;

import com.angrybird.entities.Block;
import com.angrybird.entities.Pig;
import com.angrybird.entities.Triangle;
import com.angrybird.entities.Circle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class LevelManager {
    public static void loadLevel(int levelNumber, World world, Array<Block> blocks, Array<Pig> pigs, Array<Triangle> triangles, Array<Circle> circles) {
        blocks.clear();
        pigs.clear();
        triangles.clear();
        circles.clear();

        float groundY = 2.6f;
        float groundY_2 = 1.8f;

        switch (levelNumber) {
            case 1:
                pigs.add(new Pig(world, 10, groundY + 1));
                blocks.add(new Block(world, 11, groundY + 1));
                pigs.add(new Pig(world, 12, groundY + 1));

                blocks.add(new Block(world, 10, groundY));
                pigs.add(new Pig(world, 11, groundY));
                blocks.add(new Block(world, 12, groundY));

                blocks.add(new Block(world, 10, groundY - 1));
                blocks.add(new Block(world, 11, groundY - 1));
                blocks.add(new Block(world, 12, groundY - 1));

                break;

            case 2:
                pigs.add(new Pig(world, 10, groundY_2 + 3));
                pigs.add(new Pig(world, 11, groundY_2 + 3));
                pigs.add(new Pig(world, 12, groundY_2 + 3));

                blocks.add(new Block(world, 10, groundY_2+ 2));
                blocks.add(new Block(world, 11, groundY_2+ 2));
                blocks.add(new Block(world, 12, groundY_2+ 2));

                blocks.add(new Block(world, 10, groundY_2 + 1));
                pigs.add(new Pig(world, 11, groundY_2 + 1));
                blocks.add(new Block(world, 12, groundY_2 + 1));

                blocks.add(new Block(world, 10, groundY_2 ));
                blocks.add(new Block(world, 11, groundY_2 ));
                blocks.add(new Block(world, 12, groundY_2 ));

                break;

            case 3:
                pigs.add(new Pig(world, 11, groundY_2 + 4));

                pigs.add(new Pig(world, 10, groundY_2 + 3));
                blocks.add(new Block(world, 11, groundY_2 + 3));
                pigs.add(new Pig(world, 12, groundY_2 + 3));

                blocks.add(new Block(world, 10, groundY_2  + 2));
                pigs.add(new Pig(world, 11, groundY_2  + 2));
                blocks.add(new Block(world, 12, groundY_2  + 2));

                pigs.add(new Pig(world, 10, groundY_2  + 1));
                blocks.add(new Block(world, 11, groundY_2  + 1));
                pigs.add(new Pig(world, 12, groundY_2  + 1));

                blocks.add(new Block(world, 10, groundY_2));
                blocks.add(new Block(world, 11, groundY_2));
                blocks.add(new Block(world, 12, groundY_2));

                break;

            case 4:
                blocks.add(new Block(world, 10, groundY));
                blocks.add(new Block(world, 11, groundY));
                blocks.add(new Block(world, 12, groundY));

                blocks.add(new Block(world, 10, groundY + 1));
                blocks.add(new Block(world, 11, groundY + 1));
                blocks.add(new Block(world, 12, groundY + 1));

                blocks.add(new Block(world, 11, groundY + 2));
                blocks.add(new Block(world, 12, groundY + 2));

                circles.add(new Circle(world, 10.5f, groundY + 2.5f));
                triangles.add(new Triangle(world, 11.5f, groundY + 3));

                pigs.add(new Pig(world, 10.5f, groundY + 1.5f));
                pigs.add(new Pig(world, 11.5f, groundY + 2.5f));
                pigs.add(new Pig(world, 12.5f, groundY + 2.5f));
                pigs.add(new Pig(world, 10.5f, groundY + 3.5f));
                pigs.add(new Pig(world, 11.5f, groundY + 4.5f));
                break;

            default:
                System.out.println("Invalid level number: " + levelNumber);
                break;
        }
    }
}
