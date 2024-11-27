package com.angrybird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.math.Vector2;

public class Pig {
    private Texture texture;
    private Vector2 position;
    private World world;
    private Body body;

    private float radius = 0.5f; // Radius of the pig

    public Pig(World world, float x, float y) {
        this.world = world;
        this.position = new Vector2(x, y);
        this.texture = new Texture("pig.png"); // Path to pig texture

        // Create physics body for the pig
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

        // Create a circular shape for the pig
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);  // Set radius for pig's collision

        // Define fixture properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.0f;  // Increased density for more responsive physics
        fixtureDef.friction = 0.5f; // Friction between pig and ground
        fixtureDef.restitution = 0.5f; // Increased bounciness (more responsive)

        // Create the fixture and attach to the body
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

    // Render method to draw the pig on screen with rotation
    public void render(SpriteBatch batch) {
        // Get current position and rotation from the physics body
        Vector2 pos = body.getPosition();
        float rotation = body.getAngle() * 57.295779f; // Convert radians to degrees

        batch.draw(texture,
            pos.x - radius,
            pos.y - radius,
            radius,
            radius,
            radius * 2,
            radius * 2,
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
