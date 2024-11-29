package com.angrybird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BlackBird {
    private Body body;
    private Texture texture;
    private boolean launched;
    private boolean exploded;
    private static final float EXPLOSION_RADIUS = 2f;
    private static final float EXPLOSION_FORCE = 15f;

    public BlackBird(World world, float x, float y) {
        // Create the physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        // Set user data for collision detection
        body.setUserData(this);

        // Define shape and fixture
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f;
        body.createFixture(fixtureDef);
        shape.dispose();

        // Load texture for black bird
        texture = new Texture("blackbird.png");
        launched = false;
        exploded = false;
    }

    public void render(SpriteBatch batch) {
        // Render the bird at its physics body position
        batch.draw(texture,
            body.getPosition().x - 0.5f,
            body.getPosition().y - 0.5f,
            1, 1
        );
    }

    public void explode(World world) {
        if (exploded) return;
        exploded = true;

        // Find all bodies within explosion radius
        Array<Body> bodiesInRange = new Array<>();
        world.getBodies(bodiesInRange);

        Vector2 explosionCenter = body.getPosition();

        for (Body affectedBody : bodiesInRange) {
            // Skip the black bird itself
            if (affectedBody == body) continue;

            Vector2 bodyPosition = affectedBody.getPosition();
            float distance = explosionCenter.dst(bodyPosition);

            // Check if body is within explosion radius
            if (distance <= EXPLOSION_RADIUS) {
                // Calculate explosion force based on distance
                float force = EXPLOSION_FORCE * (1 - distance / EXPLOSION_RADIUS);

                // Calculate direction away from explosion center
                Vector2 forceDirection = bodyPosition.cpy().sub(explosionCenter).nor();

                // Apply impulse to push objects away
                affectedBody.applyLinearImpulse(
                    forceDirection.scl(force),
                    bodyPosition,
                    true
                );
            }
        }
    }

    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public boolean isExploded() {
        return exploded;
    }
}
