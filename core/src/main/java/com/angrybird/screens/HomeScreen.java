package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HomeScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private Texture backgroundTexture;
    private Texture playTexture, settingsTexture, exitTexture;
    private static Music backgroundMusic;

    public HomeScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        // Load background image
        backgroundTexture = new Texture(Gdx.files.internal("home-back.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);
        stage.addActor(backgroundImage);

        // Load button images
        playTexture = new Texture(Gdx.files.internal("play-btn.png"));
        settingsTexture = new Texture(Gdx.files.internal("set-btn.png"));
        exitTexture = new Texture(Gdx.files.internal("exit-btn.png"));

        // Button dimensions
        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;

        // Create ImageButtons
        ImageButton playButton = new ImageButton(new TextureRegionDrawable(playTexture));
        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(settingsTexture));
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));

        // Set the button sizes
        playButton.setSize(buttonWidth, buttonHeight);
        settingsButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        // Adjusted button positions with reduced gap
        float baseYPlay = 330 - 36;
        float baseYSettings = 250 - 34; // Reduced by 10 pixels
        float baseYExit = 170 - 32;     // Reduced by 10 more pixels

        playButton.setPosition((1280 - playButton.getWidth()) / 2, baseYPlay);
        settingsButton.setPosition((1280 - settingsButton.getWidth()) / 2, baseYSettings);
        exitButton.setPosition((1280 - exitButton.getWidth()) / 2, baseYExit);

        // Add hover listeners to raise the buttons by 0.5%
        addHoverEffect(playButton, baseYPlay);
        addHoverEffect(settingsButton, baseYSettings);
        addHoverEffect(exitButton, baseYExit);

        // Add click listeners to the buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, HomeScreen.this));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Add buttons to the stage
        stage.addActor(playButton);
        stage.addActor(settingsButton);
        stage.addActor(exitButton);
    }

    private void addHoverEffect(ImageButton button, float originalY) {
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.moveTo(button.getX(), originalY + 0.005f * 720, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.moveTo(button.getX(), originalY, 0.1f));
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        if (backgroundMusic == null) {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3"));
            backgroundMusic.setLooping(true);
        }

        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (playTexture != null) playTexture.dispose();
        if (settingsTexture != null) settingsTexture.dispose();
        if (exitTexture != null) exitTexture.dispose();
    }
}
