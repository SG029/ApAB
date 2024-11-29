package com.angrybird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Bird {
    private Body body;
    private Texture texture;
    private boolean launched;
    private int health = 250; // Initial health for bird
    private boolean isDestroyed = false;

    public Bird(World world, float x, float y) {
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

        // Load texture (consider changing to a bird texture)
        texture = new Texture("bird.png");
        launched = false;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            isDestroyed = true;
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void render(SpriteBatch batch) {
        // Only render if not destroyed
        if (!isDestroyed) {
            batch.draw(texture,
                body.getPosition().x - 0.5f,
                body.getPosition().y - 0.5f,
                1, 1
            );
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
}
