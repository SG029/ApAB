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

        backgroundTexture = new Texture(Gdx.files.internal("settings-back.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);
        stage.addActor(backgroundImage);

        backButtonTexture = new Texture(Gdx.files.internal("back.png"));
        backHoverTexture = new Texture(Gdx.files.internal("back-h.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("exit-btn.png"));

        final ImageButton backButton = new ImageButton(
            new TextureRegionDrawable(backButtonTexture),
            new TextureRegionDrawable(backHoverTexture)
        );

        float backButtonWidth = 100;
        float backButtonHeight = 100;
        backButton.setSize(backButtonWidth, backButtonHeight);
        float backButtonYPosition = (720 - backButtonHeight) / 2;
        backButton.setPosition(20, backButtonYPosition);

        addHoverEffect(backButton, backButtonYPosition);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(previousScreen);
            }
        });

        float centerX = 1280 / 2f;

        Label volumeLabel = new Label("Volume", skin);
        volumeLabel.setPosition(centerX - volumeLabel.getWidth() / 2, 400);

        volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setSize(400, 40);
        volumeSlider.setPosition(centerX - volumeSlider.getWidth() / 2, 350);

        float currentVolume = game.getMusicVolume();
        volumeSlider.setValue(currentVolume * 100);

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!muteCheckBox.isChecked()) {
                    float volume = volumeSlider.getValue() / 100f;
                    game.setMusicVolume(volume);
                }
            }
        });

        muteCheckBox = new CheckBox("Mute", skin);
        muteCheckBox.setTransform(true);
        muteCheckBox.setScale(2f);
        muteCheckBox.setPosition(centerX - muteCheckBox.getWidth(), 280);

        muteCheckBox.setChecked(currentVolume == 0);

        muteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (muteCheckBox.isChecked()) {
                    volumeSlider.setValue(0);
                    game.setMusicVolume(0);
                } else {
                    float volume = volumeSlider.getValue() / 100f;
                    game.setMusicVolume(volume);
                }
            }
        });

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitButtonTexture));
        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;
        exitButton.setSize(buttonWidth, buttonHeight);
        float exitButtonY = 150;
        exitButton.setPosition(centerX - exitButton.getWidth() / 2, exitButtonY);

        addHoverEffect(exitButton, exitButtonY);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(backgroundImage);
        stage.addActor(backButton);
        stage.addActor(volumeLabel);
        stage.addActor(volumeSlider);
        stage.addActor(muteCheckBox);
        stage.addActor(exitButton);
    }

    private void addHoverEffect(ImageButton button, float originalY) {
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.moveTo(button.getX(), originalY + 3.6f, 0.1f));
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
