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

public class LevelScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private Skin skin;

    public LevelScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create Level 1 button
        TextButton level1Button = new TextButton("Level 1", skin);
        level1Button.setPosition(400, 300);
        level1Button.setSize(200, 50);
        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));  // Start GameScreen
            }
        });

        // Add buttons for level 2 and level 3 similarly
        TextButton level2Button = new TextButton("Level 2", skin);
        level2Button.setPosition(400, 250);
        level2Button.setSize(200, 50);

        TextButton level3Button = new TextButton("Level 3", skin);
        level3Button.setPosition(400, 200);
        level3Button.setSize(200, 50);

        stage.addActor(level1Button);
        stage.addActor(level2Button);
        stage.addActor(level3Button);
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
