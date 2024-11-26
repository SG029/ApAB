package com.angrybird.physics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysicsWorld {
    private World world;
    private Box2DDebugRenderer debugRenderer;

    public PhysicsWorld() {
        // Initialize the physics world with gravity
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();
    }

    public World getWorld() {
        return world;
    }

    public void step(float delta) {
        // Step the physics simulation
        world.step(delta, 6, 2);
    }

    public void renderDebug(Camera camera) {
        debugRenderer.render(world, camera.combined);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
