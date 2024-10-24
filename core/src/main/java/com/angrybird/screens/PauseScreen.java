package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private Skin skin;

    public PauseScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Back to menu button
        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.setPosition(400, 300);
        menuButton.setSize(200, 50);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));  // Back to HomeScreen
            }
        });

        // Resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.setPosition(400, 250);
        resumeButton.setSize(200, 50);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));  // Resume GameScreen
            }
        });

        // Retry button
        TextButton retryButton = new TextButton("Retry", skin);
        retryButton.setPosition(400, 200);
        retryButton.setSize(200, 50);
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));  // Restart GameScreen
            }
        });

        stage.addActor(menuButton);
        stage.addActor(resumeButton);
        stage.addActor(retryButton);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
