package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private final AngryBirdGame game;
    private final Screen previousScreen;
    private final Stage stage;
    private Skin skin;
    private Texture backgroundTexture, backButtonTexture, backHoverTexture, exitButtonTexture;
    private CheckBox muteCheckBox;
    private Slider volumeSlider;

    public SettingsScreen(final AngryBirdGame game, final Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load the background image
        backgroundTexture = new Texture(Gdx.files.internal("settings-back.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);
        stage.addActor(backgroundImage);

        // Load back and exit button images
        backButtonTexture = new Texture(Gdx.files.internal("back.png"));
        backHoverTexture = new Texture(Gdx.files.internal("back-h.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("exit-btn.png"));

        // Create back button with normal and hover textures
        final ImageButton backButton = new ImageButton(
            new TextureRegionDrawable(backButtonTexture),
            new TextureRegionDrawable(backHoverTexture)
        );

        float backButtonWidth = 100;
        float backButtonHeight = 100;
        backButton.setSize(backButtonWidth, backButtonHeight);
        float backButtonYPosition = (720 - backButtonHeight) / 2; // Center vertically on left side
        backButton.setPosition(20, backButtonYPosition);

        // Back button hover effect
        addHoverEffect(backButton, backButtonYPosition);

        // Back button click listener
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(previousScreen);
            }
        });

        // Center alignment for UI elements
        float centerX = 1280 / 2f;

        // Volume Label, centered above the slider
        Label volumeLabel = new Label("Volume", skin);
        volumeLabel.setPosition(centerX - volumeLabel.getWidth() / 2, 400);

        // Volume Slider, shorter and centered horizontally
        volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setSize(400, 40);  // Shorter width for a more compact look
        volumeSlider.setPosition(centerX - volumeSlider.getWidth() / 2, 350);

        // Set initial volume based on current game volume
        float currentVolume = game.getMusicVolume();
        volumeSlider.setValue(currentVolume * 100);

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Only adjust volume if not muted
                if (!muteCheckBox.isChecked()) {
                    float volume = volumeSlider.getValue() / 100f;
                    game.setMusicVolume(volume);
                }
            }
        });

        // Mute Checkbox, centered below the volume slider and enlarged
        muteCheckBox = new CheckBox("Mute", skin);
        muteCheckBox.setTransform(true); // Enable transform for scaling
        muteCheckBox.setScale(2f);       // Make the checkbox larger
        muteCheckBox.setPosition(centerX - muteCheckBox.getWidth(), 280);

        // Set initial mute state
        muteCheckBox.setChecked(currentVolume == 0);

        muteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (muteCheckBox.isChecked()) {
                    // Store the current volume before muting
                    volumeSlider.setValue(0);
                    game.setMusicVolume(0);
                } else {
                    // Restore to previous volume
                    float volume = volumeSlider.getValue() / 100f;
                    game.setMusicVolume(volume);
                }
            }
        });

        // Exit button
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitButtonTexture));
        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;
        exitButton.setSize(buttonWidth, buttonHeight);
        float exitButtonY = 150;  // Position at the bottom-center
        exitButton.setPosition(centerX - exitButton.getWidth() / 2, exitButtonY);

        // Exit button hover and click effects
        addHoverEffect(exitButton, exitButtonY);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();  // Exit the application
            }
        });

        // Add components to the stage
        stage.addActor(backgroundImage);
        stage.addActor(backButton);
        stage.addActor(volumeLabel);
        stage.addActor(volumeSlider);
        stage.addActor(muteCheckBox);
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
        Gdx.input.setInputProcessor(stage);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (backButtonTexture != null) backButtonTexture.dispose();
        if (backHoverTexture != null) backHoverTexture.dispose();
        if (exitButtonTexture != null) exitButtonTexture.dispose();
    }
}
