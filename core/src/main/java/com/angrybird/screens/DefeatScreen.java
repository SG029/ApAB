package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class DefeatScreen implements Screen {
    private final AngryBirdGame game;
    private int levelNumber;

    public DefeatScreen(final AngryBirdGame game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input to retry or return to main menu
        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game, levelNumber));
        }

        // Render defeat message
        game.batch.begin();
        // Render text or images to indicate defeat
        game.batch.end();
    }

    @Override
    public void show() {
        // Implement as needed
    }

    @Override
    public void resize(int width, int height) {
        // Implement as needed
    }

    @Override
    public void pause() {
        // Implement as needed
    }

    @Override
    public void resume() {
        // Implement as needed
    }

    @Override
    public void hide() {
        // Implement as needed
    }

    @Override
    public void dispose() {
        // Dispose resources if any
    }
}
