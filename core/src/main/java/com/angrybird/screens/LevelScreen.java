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
    private final Texture level1Texture, level2Texture, level3Texture;
    private final Texture level1LockedTexture, level2LockedTexture, level3LockedTexture;
    private final Texture backButtonTexture, backHoverTexture;

    public LevelScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        backgroundTexture = new Texture(Gdx.files.internal("images/lvl-bg.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);
        stage.addActor(backgroundImage);

        level1Texture = new Texture(Gdx.files.internal("images/lvl-1.png"));
        level2Texture = new Texture(Gdx.files.internal("images/lvl-2.png"));
        level3Texture = new Texture(Gdx.files.internal("images/lvl-3.png"));
        level1LockedTexture = new Texture(Gdx.files.internal("images/lvl-1-locked.png"));
        level2LockedTexture = new Texture(Gdx.files.internal("images/lvl-2-locked.png"));
        level3LockedTexture = new Texture(Gdx.files.internal("images/lvl-3-locked.png"));
        backButtonTexture = new Texture(Gdx.files.internal("images/back.png"));
        backHoverTexture = new Texture(Gdx.files.internal("images/back-h.png"));

        createLevelButtons();
    }

    private void createLevelButtons() {
        int highestUnlockedLevel = game.getGameStateManager().getHighestUnlockedLevel();
        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;
        float buttonGap = -10;
        float centerX = (1280 - buttonWidth) / 2;
        float startingY = 400;

        ImageButton level1Button = createLevelButton(1, level1Texture, level1LockedTexture,
            centerX, startingY, buttonWidth, buttonHeight, highestUnlockedLevel);

        ImageButton level2Button = createLevelButton(2, level2Texture, level2LockedTexture,
            centerX, startingY - (buttonHeight + buttonGap), buttonWidth, buttonHeight, highestUnlockedLevel);

        ImageButton level3Button = createLevelButton(3, level3Texture, level3LockedTexture,
            centerX, startingY - 2 * (buttonHeight + buttonGap), buttonWidth, buttonHeight, highestUnlockedLevel);

        ImageButton backButton = new ImageButton(
            new TextureRegionDrawable(backButtonTexture),
            new TextureRegionDrawable(backHoverTexture)
        );
        backButton.setSize(80, 80);
        float centerYBack = (720 - backButton.getHeight()) / 2;
        backButton.setPosition(50, centerYBack);
        addHoverEffect(backButton, centerYBack);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        });

        stage.addActor(backButton);
        stage.addActor(level1Button);
        stage.addActor(level2Button);
        stage.addActor(level3Button);
    }

    private ImageButton createLevelButton(int levelNumber, Texture unlockedTexture,
                                          Texture lockedTexture, float x, float y,
                                          float width, float height, int highestUnlockedLevel) {
        boolean isUnlocked = game.getGameStateManager().isLevelUnlocked(levelNumber);
        Texture buttonTexture = isUnlocked ? unlockedTexture : lockedTexture;
        ImageButton button = new ImageButton(new TextureRegionDrawable(buttonTexture));
        button.setSize(width, height);
        button.setPosition(x, y);

        if (isUnlocked) {
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game, levelNumber));
                }
            });
            addHoverEffect(button, button.getY());
        }

        return button;
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
        if (level1Texture != null) level1Texture.dispose();
        if (level2Texture != null) level2Texture.dispose();
        if (level3Texture != null) level3Texture.dispose();
        if (level1LockedTexture != null) level1LockedTexture.dispose();
        if (level2LockedTexture != null) level2LockedTexture.dispose();
        if (level3LockedTexture != null) level3LockedTexture.dispose();
        if (backButtonTexture != null) backButtonTexture.dispose();
        if (backHoverTexture != null) backHoverTexture.dispose();
    }
}
