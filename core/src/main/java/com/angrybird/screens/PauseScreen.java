package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private final int levelNumber; // Stores the current level number
    private Texture backgroundTexture, resumeTexture, menuTexture, settingsTexture, retryTexture, exitTexture;

    /**
     * Constructor for PauseScreen.
     *
     * @param game        The main game instance.
     * @param levelNumber The current level number.
     */
    public PauseScreen(final AngryBirdGame game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
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

        // Resume Button: Resumes the game by setting GameScreen with the current level
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, levelNumber));  // Resume GameScreen with current level
            }
        });

        // Menu Button: Returns to the HomeScreen
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));  // Back to HomeScreen
            }
        });

        // Settings Button: Opens SettingsScreen and passes the current PauseScreen for returning after settings
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, PauseScreen.this));  // Go to SettingsScreen
            }
        });

        // Retry Button: Restarts the current level by setting GameScreen with the current level
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, levelNumber));  // Restart GameScreen with current level
            }
        });

        // Exit Button: Exits the game
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

    /**
     * Adds a hover effect to a button that moves it slightly upwards when hovered.
     *
     * @param button     The button to add the effect to.
     * @param originalY The original Y position of the button.
     */
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
        // Set the input processor to the current stage
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport when the screen size changes
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Implement pause logic if necessary
    }

    @Override
    public void resume() {
        // Implement resume logic if necessary
    }

    @Override
    public void hide() {
        // Clear the input processor when the screen is hidden
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // Dispose of assets to free resources
        stage.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (resumeTexture != null) resumeTexture.dispose();
        if (menuTexture != null) menuTexture.dispose();
        if (settingsTexture != null) settingsTexture.dispose();
        if (retryTexture != null) retryTexture.dispose();
        if (exitTexture != null) exitTexture.dispose();
    }
}
