package com.angrybird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Bird {
    private Body body;
    private Texture texture;
    private boolean launched;

    public Bird(World world, float x, float y) {
        // Create the physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        // Define shape and fixture
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);
        shape.dispose();

        // Load texture
        texture = new Texture("pig.png");
        launched = false;
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

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }
}
