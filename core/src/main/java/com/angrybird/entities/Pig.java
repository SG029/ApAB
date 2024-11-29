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

    private float radius = 0.5f;
    private int health = 200;
    private boolean isDestroyed = false;

    private static final float DAMAGE_VELOCITY_THRESHOLD = 3f;
    private static final int COLLISION_DAMAGE = 20;

    public Pig(World world, float x, float y) {
        this.world = world;
        this.position = new Vector2(x, y);
        this.texture = new Texture("pig.png");
        createBody();
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.angle = 0;
        bodyDef.allowSleep = false;
        bodyDef.awake = true;

        body = world.createBody(bodyDef);

        body.setUserData(this);
        body.setLinearDamping(0.5f);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.3f;

        body.createFixture(fixtureDef);
        shape.dispose();

        position.set(body.getPosition());
    }

    public void takeDamage(Vector2 velocity) {
        float collisionSpeed = Math.abs(velocity.len());

        if (collisionSpeed > DAMAGE_VELOCITY_THRESHOLD) {
            int damage = Math.min(
                (int)(collisionSpeed * COLLISION_DAMAGE),
                100
            );

            health -= damage;

            if (health <= 0) {
                health = 0;
                isDestroyed = true;
            }
        }
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

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        texture.dispose();

        if (body != null) {
            world.destroyBody(body);
        }
    }

    public void render(SpriteBatch batch) {
        if (!isDestroyed) {
            Vector2 pos = body.getPosition();
            float rotation = body.getAngle() * 57.295779f;

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
}
