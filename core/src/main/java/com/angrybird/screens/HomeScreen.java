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

public class HomeScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private Skin skin;

    public HomeScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);  // Allow stage to handle input events

        skin = new Skin(Gdx.files.internal("uiskin.json"));  // Load skin for buttons

        // Create Play button
        TextButton playButton = new TextButton("Play", skin);
        playButton.setPosition(400, 300);
        playButton.setSize(200, 50);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game));
            }
        });

        // Create Exit button
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setPosition(400, 200);
        exitButton.setSize(200, 50);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Create Settings button
        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.setPosition(400, 250);
        settingsButton.setSize(200, 50);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        // Add buttons to stage
        stage.addActor(playButton);
        stage.addActor(exitButton);
        stage.addActor(settingsButton);
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
