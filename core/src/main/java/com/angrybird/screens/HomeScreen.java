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
    private Texture playTexture, settingsTexture, exitTexture, openButtonTexture;
    private Music backgroundMusic;

    public HomeScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        backgroundTexture = new Texture(Gdx.files.internal("images/home-back.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);
        stage.addActor(backgroundImage);

        playTexture = new Texture(Gdx.files.internal("images/play-btn.png"));
        settingsTexture = new Texture(Gdx.files.internal("images/set-btn.png"));
        exitTexture = new Texture(Gdx.files.internal("images/exit-btn.png"));
        openButtonTexture = new Texture(Gdx.files.internal("images/open-btn.png"));

        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;

        ImageButton playButton = new ImageButton(new TextureRegionDrawable(playTexture));
        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(settingsTexture));
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));
        ImageButton openSaveButton = new ImageButton(new TextureRegionDrawable(openButtonTexture));

        playButton.setSize(buttonWidth, buttonHeight);
        settingsButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);
        openSaveButton.setSize(buttonWidth, buttonHeight);

        float baseYPlay = 330 - 36;
        float baseYSettings = 250 - 34;
        float baseYOpen = 170 - 32;
        float baseYExit = 90 - 32;

        playButton.setPosition((1280 - playButton.getWidth()) / 2, baseYPlay);
        settingsButton.setPosition((1280 - settingsButton.getWidth()) / 2, baseYSettings);
        openSaveButton.setPosition((1280 - openSaveButton.getWidth()) / 2, baseYOpen);
        exitButton.setPosition((1280 - exitButton.getWidth()) / 2, baseYExit);

        addHoverEffect(playButton, baseYPlay);
        addHoverEffect(settingsButton, baseYSettings);
        addHoverEffect(openSaveButton, baseYOpen);
        addHoverEffect(exitButton, baseYExit);

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

        openSaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SaveScreen(game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(playButton);
        stage.addActor(settingsButton);
        stage.addActor(openSaveButton);
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
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    public void resume() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (playTexture != null) playTexture.dispose();
        if (settingsTexture != null) settingsTexture.dispose();
        if (exitTexture != null) exitTexture.dispose();
        if (openButtonTexture != null) openButtonTexture.dispose();
        if (backgroundMusic != null) backgroundMusic.dispose();
    }
}
