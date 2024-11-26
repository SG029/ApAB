package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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

        // Load back button images (normal and hover)
        backButtonTexture = new Texture(Gdx.files.internal("back.png"));
        backHoverTexture = new Texture(Gdx.files.internal("back-h.png"));

        // Create back button with normal and hover textures
        ImageButton backButton = new ImageButton(
            new TextureRegionDrawable(backButtonTexture),
            new TextureRegionDrawable(backHoverTexture)
        );

        backButton.setSize(100, 100);
        float backButtonYPosition = (720 - backButton.getHeight()) / 2; // Center vertically on left side
        backButton.setPosition(20, backButtonYPosition);

        backButton.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                backButton.addAction(Actions.moveTo(backButton.getX(), backButtonYPosition + 10, 0.2f));
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                backButton.addAction(Actions.moveTo(backButton.getX(), backButtonYPosition, 0.2f));
            }

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
        volumeSlider.setValue(game.getMusicVolume() * 100);

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!muteCheckBox.isChecked()) {  // Only adjust volume if not muted
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
        muteCheckBox.setChecked(game.getMusicVolume() == 0);

        muteCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (muteCheckBox.isChecked()) {
                    game.setMusicVolume(0);
                    volumeSlider.setValue(0);  // Update slider visually
                } else {
                    float volume = volumeSlider.getValue() / 100f;
                    game.setMusicVolume(volume);
                }
            }
        });

        // Load exit button image
        exitButtonTexture = new Texture(Gdx.files.internal("exit-btn.png"));
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitButtonTexture));
        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;
        exitButton.setSize(buttonWidth, buttonHeight);  // Larger exit button
        float exitButtonY = 150;  // Position at the bottom-center
        exitButton.setPosition(centerX - exitButton.getWidth() / 2, exitButtonY);

        // Add hover effect to raise exit button by a few pixels on hover
        exitButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitButton.addAction(Actions.moveTo(exitButton.getX(), exitButtonY + 10, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitButton.addAction(Actions.moveTo(exitButton.getX(), exitButtonY, 0.1f));
            }

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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (backButtonTexture != null) backButtonTexture.dispose();
        if (backHoverTexture != null) backHoverTexture.dispose();
        if (exitButtonTexture != null) exitButtonTexture.dispose();
    }
}
