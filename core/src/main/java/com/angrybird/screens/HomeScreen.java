package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HomeScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private Texture backgroundTexture;
    private Texture playTexture, settingsTexture, exitTexture;
    private Music backgroundMusic;

    public HomeScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        // Load background image
        backgroundTexture = new Texture(Gdx.files.internal("images/home-back.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);
        stage.addActor(backgroundImage);

        // Load button images
        playTexture = new Texture(Gdx.files.internal("images/play-btn.png"));
        settingsTexture = new Texture(Gdx.files.internal("images/set-btn.png"));
        exitTexture = new Texture(Gdx.files.internal("images/exit-btn.png"));

        // Button dimensions
        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;

        // Create ImageButtons
        ImageButton playButton = new ImageButton(new TextureRegionDrawable(playTexture));
        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(settingsTexture));
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));

        // Set button sizes
        playButton.setSize(buttonWidth, buttonHeight);
        settingsButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        // Position buttons vertically with spacing
        float baseYPlay = 330 - 36;
        float baseYSettings = 250 - 34;
        float baseYExit = 170 - 32;

        playButton.setPosition((1280 - playButton.getWidth()) / 2, baseYPlay);
        settingsButton.setPosition((1280 - settingsButton.getWidth()) / 2, baseYSettings);
        exitButton.setPosition((1280 - exitButton.getWidth()) / 2, baseYExit);

        // Add hover listeners
        addHoverEffect(playButton, baseYPlay);
        addHoverEffect(settingsButton, baseYSettings);
        addHoverEffect(exitButton, baseYExit);

        // Add click listeners
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game));  // Transition to LevelScreen
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
                // Move button up by 3.6 pixels (0.005 * 720)
                button.addAction(Actions.moveTo(button.getX(), originalY + 3.6f, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Move button back to original Y position
                button.addAction(Actions.moveTo(button.getX(), originalY, 0.1f));
            }
        });
    }

    @Override
    public void show() {
        // Set the input processor to the stage
        Gdx.input.setInputProcessor(stage);

        // Initialize and play background music if not already playing
        if (backgroundMusic == null) {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/bgm.mp3"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(game.getMusicVolume());
        }

        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Optional: Pause the background music
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    public void resume() {
        // Optional: Resume the background music
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    @Override
    public void hide() {
        // Dispose of the input processor when the screen is hidden
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // Dispose of assets
        stage.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (playTexture != null) playTexture.dispose();
        if (settingsTexture != null) settingsTexture.dispose();
        if (exitTexture != null) exitTexture.dispose();
        if (backgroundMusic != null) backgroundMusic.dispose();
    }
}
