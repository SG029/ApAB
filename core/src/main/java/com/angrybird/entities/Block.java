package com.angrybird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Block {
    private Body body;
    private Texture texture;
    private int health;

    public Block(World world, float x, float y) {
        // Initialize health
        health = 100;

        // Create the physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        // Define shape and fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        body.createFixture(fixtureDef);
        shape.dispose();

        // Load texture
        texture = new Texture("images/block.png");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x - 0.5f, body.getPosition().y - 0.5f, 1, 1);
    }

    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }
}
