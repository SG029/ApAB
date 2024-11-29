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
    private final int levelNumber;
    private final GameScreen previousGameScreen;
    private Texture backgroundTexture, resumeTexture, menuTexture, settingsTexture, retryTexture, exitTexture, saveTexture;

    public PauseScreen(final AngryBirdGame game, int levelNumber, GameScreen previousGameScreen) {
        this.game = game;
        this.levelNumber = levelNumber;
        this.previousGameScreen = previousGameScreen;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("pause-bg.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage);

        resumeTexture = new Texture(Gdx.files.internal("res-btn.png"));
        menuTexture = new Texture(Gdx.files.internal("menu-btn.png"));
        settingsTexture = new Texture(Gdx.files.internal("set-btn.png"));
        retryTexture = new Texture(Gdx.files.internal("retry-btn.png"));
        exitTexture = new Texture(Gdx.files.internal("exit-btn.png"));
        saveTexture = new Texture(Gdx.files.internal("save-btn.png"));

        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(resumeTexture));
        ImageButton menuButton = new ImageButton(new TextureRegionDrawable(menuTexture));
        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(settingsTexture));
        ImageButton retryButton = new ImageButton(new TextureRegionDrawable(retryTexture));
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));
        ImageButton saveButton = new ImageButton(new TextureRegionDrawable(saveTexture));

        float buttonWidth = 180 * 1.0f;
        float buttonHeight = 70 * 1.0f;
        float buttonGap = -5;

        float centerX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        float startingY = Gdx.graphics.getHeight() * 0.6f;

        resumeButton.setSize(buttonWidth, buttonHeight);
        menuButton.setSize(buttonWidth, buttonHeight);
        settingsButton.setSize(buttonWidth, buttonHeight);
        retryButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);
        saveButton.setSize(buttonWidth, buttonHeight);

        resumeButton.setPosition(centerX, startingY);
        menuButton.setPosition(centerX, startingY - (buttonHeight + buttonGap));
        settingsButton.setPosition(centerX, startingY - 2 * (buttonHeight + buttonGap));
        saveButton.setPosition(centerX, startingY - 3 * (buttonHeight + buttonGap));
        retryButton.setPosition(centerX, startingY - 4 * (buttonHeight + buttonGap));
        exitButton.setPosition(centerX, startingY - 5 * (buttonHeight + buttonGap));

        addHoverEffect(resumeButton, resumeButton.getY());
        addHoverEffect(menuButton, menuButton.getY());
        addHoverEffect(settingsButton, settingsButton.getY());
        addHoverEffect(saveButton, saveButton.getY());
        addHoverEffect(retryButton, retryButton.getY());
        addHoverEffect(exitButton, exitButton.getY());

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(previousGameScreen);
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, PauseScreen.this));
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, levelNumber));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveCurrentGameState();
            }
        });

        stage.addActor(resumeButton);
        stage.addActor(menuButton);
        stage.addActor(settingsButton);
        stage.addActor(saveButton);
        stage.addActor(retryButton);
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

    private void saveCurrentGameState() {
        game.getGameStateManager().setCurrentLevel(levelNumber);

        if (previousGameScreen != null) {
            game.getGameStateManager().updateLevelProgress(
                previousGameScreen.getCurrentScore(),
                previousGameScreen.getRemainingPigs(),
                previousGameScreen.getTimeRemaining()
            );
        }

        game.saveGame();
        System.out.println("Game state saved successfully!");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
        if (saveTexture != null) saveTexture.dispose();
    }
}
