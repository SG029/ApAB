package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private Texture backgroundTexture, resumeTexture, menuTexture, settingsTexture, retryTexture, exitTexture;

    public PauseScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

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

        // Set button sizes
        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;
        float buttonGap = -10; // Gap between buttons

        // Center alignment for the X axis
        float centerX = (1280 - buttonWidth) / 2;

        // Position buttons vertically in the center with equal spacing
        resumeButton.setSize(buttonWidth, buttonHeight);
        menuButton.setSize(buttonWidth, buttonHeight);
        settingsButton.setSize(buttonWidth, buttonHeight);
        retryButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        // Calculate the starting Y position for the top button
        float startingY = 400;

        resumeButton.setPosition(centerX, startingY);
        menuButton.setPosition(centerX, startingY - (buttonHeight + buttonGap));
        settingsButton.setPosition(centerX, startingY - 2 * (buttonHeight + buttonGap));
        retryButton.setPosition(centerX, startingY - 3 * (buttonHeight + buttonGap));
        exitButton.setPosition(centerX, startingY - 4 * (buttonHeight + buttonGap));

        // Add hover effect to each button
        addHoverEffect(resumeButton, resumeButton.getY());
        addHoverEffect(menuButton, menuButton.getY());
        addHoverEffect(settingsButton, settingsButton.getY());
        addHoverEffect(retryButton, retryButton.getY());
        addHoverEffect(exitButton, exitButton.getY());

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
        // Reset the input processor to this stage
        Gdx.input.setInputProcessor(stage);
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
