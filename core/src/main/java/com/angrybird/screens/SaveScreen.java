package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.angrybird.save.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SaveScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private final Texture backgroundTexture;
    private final Texture save1Texture, save2Texture, save3Texture, backButtonTexture, backHoverTexture;

    public SaveScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        backgroundTexture = new Texture(Gdx.files.internal("images/save-bg.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);
        stage.addActor(backgroundImage);

        save1Texture = new Texture(Gdx.files.internal("images/s1.png"));
        save2Texture = new Texture(Gdx.files.internal("images/s2.png"));
        save3Texture = new Texture(Gdx.files.internal("images/s3.png"));
        backButtonTexture = new Texture(Gdx.files.internal("images/back.png"));
        backHoverTexture = new Texture(Gdx.files.internal("images/back-h.png"));

        ImageButton save1Button = new ImageButton(new TextureRegionDrawable(save1Texture));
        ImageButton save2Button = new ImageButton(new TextureRegionDrawable(save2Texture));
        ImageButton save3Button = new ImageButton(new TextureRegionDrawable(save3Texture));

        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;
        float buttonGap = -10;
        float centerX = (1280 - buttonWidth) / 2;
        float startingY = 400;

        save1Button.setSize(buttonWidth, buttonHeight);
        save2Button.setSize(buttonWidth, buttonHeight);
        save3Button.setSize(buttonWidth, buttonHeight);

        save1Button.setPosition(centerX, startingY);
        save2Button.setPosition(centerX, startingY - (buttonHeight + buttonGap));
        save3Button.setPosition(centerX, startingY - 2 * (buttonHeight + buttonGap));

        ImageButton backButton = new ImageButton(
            new TextureRegionDrawable(backButtonTexture),
            new TextureRegionDrawable(backHoverTexture)
        );
        backButton.setSize(80, 80);
        float centerYBack = (720 - backButton.getHeight()) / 2;
        backButton.setPosition(50, centerYBack);

        addHoverEffect(backButton, centerYBack);
        addHoverEffect(save1Button, save1Button.getY());
        addHoverEffect(save2Button, save2Button.getY());
        addHoverEffect(save3Button, save3Button.getY());

        save1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadSaveSlot(1);
            }
        });

        save2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadSaveSlot(2);
            }
        });

        save3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadSaveSlot(3);
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        });

        stage.addActor(backButton);
        stage.addActor(save1Button);
        stage.addActor(save2Button);
        stage.addActor(save3Button);
    }

    private void loadSaveSlot(int slotNumber) {
        GameStateManager savedState = GameStateManager.loadGameState();
        if (savedState.hasProgress()) {
            int currentLevel = savedState.getCurrentLevel();
            game.setScreen(new GameScreen(game, currentLevel));
        } else {
            System.out.println("No saved game found in slot " + slotNumber);
        }
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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (save1Texture != null) save1Texture.dispose();
        if (save2Texture != null) save2Texture.dispose();
        if (save3Texture != null) save3Texture.dispose();
        if (backButtonTexture != null) backButtonTexture.dispose();
        if (backHoverTexture != null) backHoverTexture.dispose();
    }
}
