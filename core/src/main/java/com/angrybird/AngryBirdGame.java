package com.angrybird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.angrybird.screens.HomeScreen;

public class AngryBirdGame extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new HomeScreen(this));  // Start with the HomeScreen
    }

    @Override
    public void render() {
        super.render();  // Calls the render method of the current screen
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
