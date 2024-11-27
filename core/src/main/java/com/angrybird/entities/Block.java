package com.angrybird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Block {
    private Texture texture;
    private Vector2 position;
    private World world;
    private Body body;

    private float width = 1f;  // Width of the block (in world units)
    private float height = 1f; // Height of the block (in world units)

    public Block(World world, float x, float y) {
        this.world = world;
        this.position = new Vector2(x, y);
        this.texture = new Texture("block.png"); // Path to block texture

        // Create physics body for the block
        createBody();
    }

    private void createBody() {
        // Define body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;  // Ensure it's a dynamic body
        bodyDef.position.set(position);
        bodyDef.angle = 0; // Ensure no initial rotation
        bodyDef.allowSleep = false; // Prevent sleeping to keep it responsive
        bodyDef.awake = true; // Keep the body awake

        // Create the body in the world
        body = world.createBody(bodyDef);

        // Set user data for collision detection
        body.setUserData(this);

        // Create a box shape for the block (match the size of the texture)
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);  // Half width and height of the block

        // Define fixture properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.0f;  // Increased density for more responsive physics
        fixtureDef.friction = 0.5f; // Friction between block and ground
        fixtureDef.restitution = 0.5f; // Increased bounciness (more responsive)

        // Create the fixture and attach it to the body
        body.createFixture(fixtureDef);
        shape.dispose();  // Dispose shape after use to free up memory

        // Update position to reflect body's initial position
        position.set(body.getPosition());
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        // Always get the most up-to-date position from the physics body
        return body.getPosition();
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        texture.dispose();  // Dispose of texture when no longer needed
    }

    // Render method to draw the block on screen with the correct size and rotation
    public void render(SpriteBatch batch) {
        // Get current position and rotation from the physics body
        Vector2 pos = body.getPosition();
        float rotation = body.getAngle() * 57.295779f; // Convert radians to degrees

        batch.draw(texture,
            pos.x - width / 2,
            pos.y - height / 2,
            width / 2,
            height / 2,
            width,
            height,
            1f,
            1f,
            rotation,
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false
        );
    }
}
