package com.angrybird.physics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysicsWorld {
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private boolean isPaused = false;
    private float accumulatedTime = 0f;

    public PhysicsWorld() {
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();
    }

    public World getWorld() {
        return world;
    }

    public void step(float delta) {
        if (!isPaused) {
            world.step(delta, 6, 2);
        } else {
            accumulatedTime += delta;
        }
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
        accumulatedTime = 0f;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void renderDebug(Camera camera) {
        debugRenderer.render(world, camera.combined);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
