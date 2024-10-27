package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private Texture backgroundTexture, resumeTexture, menuTexture, settingsTexture, retryTexture, exitTexture;

    public PauseScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        // Load the background texture and set it as an image on the stage
        backgroundTexture = new Texture(Gdx.files.internal("pause-bg.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);  // Adjust to match screen size
        stage.addActor(backgroundImage);  // Add background to the stage

        // Load button textures
        resumeTexture = new Texture(Gdx.files.internal("res-btn.png"));
        menuTexture = new Texture(Gdx.files.internal("menu-btn.png"));
        settingsTexture = new Texture(Gdx.files.internal("set-btn.png"));
        retryTexture = new Texture(Gdx.files.internal("retry-btn.png"));
        exitTexture = new Texture(Gdx.files.internal("exit-btn.png"));

        // Create image buttons
        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(resumeTexture));
        ImageButton menuButton = new ImageButton(new TextureRegionDrawable(menuTexture));
        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(settingsTexture));
        ImageButton retryButton = new ImageButton(new TextureRegionDrawable(retryTexture));
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));

        // Set button sizes to be larger for better visibility
        float buttonWidth = 180;
        float buttonHeight = 180;
        resumeButton.setSize(buttonWidth, buttonHeight);
        menuButton.setSize(buttonWidth, buttonHeight);
        settingsButton.setSize(buttonWidth, buttonHeight);
        retryButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        // Arrange buttons in a staggered layout
        // Top row (Resume and Settings), shifted slightly down
        float topRowY = 380;  // Lowered from 420 to 380
        float centerX = 1280 / 2 - (buttonWidth + 20); // Center first button of top row
        resumeButton.setPosition(centerX, topRowY);
        settingsButton.setPosition(centerX + buttonWidth + 40, topRowY);  // Spaced by 40px

        // Middle row (Menu and Retry) stays in its current position
        float middleRowY = 220;
        menuButton.setPosition(centerX, middleRowY);
        retryButton.setPosition(centerX + buttonWidth + 40, middleRowY);  // Spaced by 40px

        // Bottom row (Exit Button) stays in its current position
        float exitButtonY = 50;  // Positioned below the middle row
        exitButton.setPosition(centerX + (buttonWidth + 40) / 2, exitButtonY);

        // Add click listeners for each button
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));  // Resume GameScreen
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));  // Back to HomeScreen
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, PauseScreen.this));  // Go to SettingsScreen
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));  // Restart GameScreen
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();  // Exit the game
            }
        });

        // Add buttons to the stage after the background
        stage.addActor(resumeButton);
        stage.addActor(menuButton);
        stage.addActor(settingsButton);
        stage.addActor(retryButton);
        stage.addActor(exitButton);
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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (resumeTexture != null) resumeTexture.dispose();
        if (menuTexture != null) menuTexture.dispose();
        if (settingsTexture != null) settingsTexture.dispose();
        if (retryTexture != null) retryTexture.dispose();
        if (exitTexture != null) exitTexture.dispose();
    }
}
