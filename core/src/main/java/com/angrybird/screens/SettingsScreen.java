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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private final AngryBirdGame game;
    private final Screen previousScreen;
    private final Stage stage;
    private Skin skin;
    private Texture backgroundTexture, backButtonTexture, backHoverTexture, exitButtonTexture;

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

        // Load exit button image
        exitButtonTexture = new Texture(Gdx.files.internal("exit-btn.png"));

        // Create back button with normal and hover textures
        ImageButton backButton = new ImageButton(
            new TextureRegionDrawable(backButtonTexture),
            new TextureRegionDrawable(backHoverTexture)
        );
        backButton.setSize(80, 80);
        float originalYPosition = (720 - backButton.getHeight()) / 2;
        backButton.setPosition(50, originalYPosition);

        backButton.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                backButton.addAction(Actions.moveTo(backButton.getX(), originalYPosition + 0.01f * 720, 0.2f));
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                backButton.addAction(Actions.moveTo(backButton.getX(), originalYPosition, 0.2f));
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(previousScreen);
            }
        });

        // Center alignment for UI elements
        float centerX = 1280 / 2f;

        // Volume Slider, centered horizontally, and increased by 200%
        Slider volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setSize(400, 100);  // Increase size by 200%
        volumeSlider.setPosition(centerX - volumeSlider.getWidth() / 2, 300);
        volumeSlider.setValue(game.getMusicVolume() * 100);

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue() / 100f;
                game.setMusicVolume(volume);
            }
        });

        // Mute Checkbox, centered horizontally, and increased by 200%
        CheckBox muteCheckBox = new CheckBox("Mute", skin);
        muteCheckBox.setTransform(true);  // Enable transform to scale the checkbox
        muteCheckBox.setScale(2f);        // Scale to 200%
        muteCheckBox.setPosition(centerX - muteCheckBox.getWidth(), 250);  // Adjusted for increased size
        muteCheckBox.setChecked(game.getMusicVolume() == 0);

        muteCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (muteCheckBox.isChecked()) {
                    game.setMusicVolume(0);
                } else {
                    game.setMusicVolume(volumeSlider.getValue() / 100f);
                }
            }
        });

        // Exit Button using an image, centered horizontally and increased by 200%
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitButtonTexture));
        exitButton.setSize(250, 250);  // Increase size by 200%
        float exitButtonOriginalY = 40;  // Position near the bottom
        exitButton.setPosition(centerX - exitButton.getWidth() / 2, exitButtonOriginalY);

        // Add hover effect to raise exit button by 0.5% on hover
        exitButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitButton.addAction(Actions.moveTo(exitButton.getX(), exitButtonOriginalY + 0.005f * 720, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitButton.addAction(Actions.moveTo(exitButton.getX(), exitButtonOriginalY, 0.1f));
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (exitButton.isPressed()) {
                    Gdx.app.exit();
                }
            }
        });

        stage.addActor(backButton);
        stage.addActor(volumeSlider);
        stage.addActor(muteCheckBox);
        stage.addActor(exitButton);
    }

    @Override
    public void show() {
        // No action to start music here to prevent multiple play instances
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
