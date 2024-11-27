package com.angrybird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Triangle {
    private Texture texture;
    private Vector2 position;
    private World world;
    private Body body;

    public Triangle(World world, float x, float y) {
        this.world = world;
        this.position = new Vector2(x, y);
        this.texture = new Texture("triangle_small.png"); // Path to triangle texture

        // Create physics body
        createBody();
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(new float[] {-0.5f, 0, 0.5f, 0, 0f, 0.5f}); // Triangle vertices

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        texture.dispose();
    }
}
