package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private final Texture backgroundTexture;
    private final Texture level1Texture, level2Texture, level3Texture, backButtonTexture, backHoverTexture;

    /**
     * Constructor for LevelScreen.
     *
     * @param game The main game instance.
     */
    public LevelScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        // Load the background image
        backgroundTexture = new Texture(Gdx.files.internal("images/lvl-bg.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);  // Adjust the background size to match the screen resolution
        stage.addActor(backgroundImage);  // Add background to the stage

        // Load level button images
        level1Texture = new Texture(Gdx.files.internal("images/lvl-1.png"));
        level2Texture = new Texture(Gdx.files.internal("images/lvl-2.png"));
        level3Texture = new Texture(Gdx.files.internal("images/lvl-3.png"));
        backButtonTexture = new Texture(Gdx.files.internal("images/back.png"));
        backHoverTexture = new Texture(Gdx.files.internal("images/back-h.png"));

        // Create image buttons for the levels
        ImageButton level1Button = new ImageButton(new TextureRegionDrawable(level1Texture));
        ImageButton level2Button = new ImageButton(new TextureRegionDrawable(level2Texture));
        ImageButton level3Button = new ImageButton(new TextureRegionDrawable(level3Texture));

        // Set button sizes
        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;
        float buttonGap = -10; // Gap between buttons

        // Center alignment for the X axis
        float centerX = (1280 - buttonWidth) / 2;

        // Position buttons vertically with spacing
        float startingY = 400;

        level1Button.setSize(buttonWidth, buttonHeight);
        level2Button.setSize(buttonWidth, buttonHeight);
        level3Button.setSize(buttonWidth, buttonHeight);

        level1Button.setPosition(centerX, startingY);
        level2Button.setPosition(centerX, startingY - (buttonHeight + buttonGap));
        level3Button.setPosition(centerX, startingY - 2 * (buttonHeight + buttonGap));

        // Create the back button
        ImageButton backButton = new ImageButton(
            new TextureRegionDrawable(backButtonTexture),
            new TextureRegionDrawable(backHoverTexture)
        );
        backButton.setSize(80, 80);
        float centerYBack = (720 - backButton.getHeight()) / 2;
        backButton.setPosition(50, centerYBack);

        // Add hover animation to back button
        addHoverEffect(backButton, centerYBack);

        // Add hover animations to level buttons
        addHoverEffect(level1Button, level1Button.getY());
        addHoverEffect(level2Button, level2Button.getY());
        addHoverEffect(level3Button, level3Button.getY());

        // Add click listeners
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));  // Go back to the HomeScreen
            }
        });

        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 1));  // Start Level 1
            }
        });

        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 2));  // Start Level 2
            }
        });

        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 3));  // Start Level 3
            }
        });

        // Add buttons to the stage
        stage.addActor(backButton);
        stage.addActor(level1Button);
        stage.addActor(level2Button);
        stage.addActor(level3Button);
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
        // Clear the screen
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
        if (level1Texture != null) level1Texture.dispose();
        if (level2Texture != null) level2Texture.dispose();
        if (level3Texture != null) level3Texture.dispose();
        if (backButtonTexture != null) backButtonTexture.dispose();
        if (backHoverTexture != null) backHoverTexture.dispose();
    }
}
