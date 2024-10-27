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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelScreen implements Screen {
    private final AngryBirdGame game;
    private final Stage stage;
    private Texture backgroundTexture, level1Texture, level2Texture, level3Texture, backButtonTexture, backHoverTexture;

    public LevelScreen(final AngryBirdGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        // Load the background image
        backgroundTexture = new Texture(Gdx.files.internal("lvl-bg.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(1280, 720);  // Adjust the background size to match the screen resolution
        stage.addActor(backgroundImage);  // Add the background first, so it renders behind other UI elements

        // Load level button images
        level1Texture = new Texture(Gdx.files.internal("lvl-1.png"));
        level2Texture = new Texture(Gdx.files.internal("lvl-2.png"));
        level3Texture = new Texture(Gdx.files.internal("lvl-3.png"));
        backButtonTexture = new Texture(Gdx.files.internal("back.png"));
        backHoverTexture = new Texture(Gdx.files.internal("back-h.png"));

        // Create image buttons for the levels
        ImageButton level1Button = new ImageButton(new TextureRegionDrawable(level1Texture));
        ImageButton level2Button = new ImageButton(new TextureRegionDrawable(level2Texture));
        ImageButton level3Button = new ImageButton(new TextureRegionDrawable(level3Texture));

        // Set button sizes
        float buttonWidth = 200 * 1.2f;
        float buttonHeight = 80 * 1.2f;
        level1Button.setSize(buttonWidth, buttonHeight);
        level2Button.setSize(buttonWidth, buttonHeight);
        level3Button.setSize(buttonWidth, buttonHeight);

        // Center align the buttons and position them vertically with a 10-pixel gap
//        float centerX = (1280 - level1Button.getWidth()) / 2;
//        level1Button.setPosition(centerX, 370);
//        level2Button.setPosition(centerX, level1Button.getY() - level1Button.getHeight() - 10);  // 10px gap
//        level3Button.setPosition(centerX, level2Button.getY() - level2Button.getHeight() - 10);  // 10px gap

        float baseY1 = 400 - 36;
        float baseY2 = 320 - 40; // Reduced by 10 pixels
        float baseY3 = 240 - 44;     // Reduced by 10 more pixels

        level1Button.setPosition((1280 - level1Button.getWidth()) / 2, baseY1);
        level2Button.setPosition((1280 - level2Button.getWidth()) / 2, baseY2);
        level3Button.setPosition((1280 - level3Button.getWidth()) / 2, baseY3);

        // Add hover animations to level buttons
        addHoverEffect(level1Button,baseY1);
        addHoverEffect(level2Button,baseY2);
        addHoverEffect(level3Button,baseY3);

        // Create the back button
        ImageButton backButton = new ImageButton(
            new TextureRegionDrawable(backButtonTexture),
            new TextureRegionDrawable(backHoverTexture)
        );
        backButton.setSize(80, 80);
        float centerYBack = (720 - backButton.getHeight()) / 2;  // Center the back button on the Y-axis
        backButton.setPosition(50, centerYBack);

        // Add hover animation to back button
        addHoverEffect(backButton,centerYBack);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));  // Go back to the HomeScreen
            }
        });

        // Add click listeners for each level button
        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));  // Go to GameScreen (Level 1)
            }
        });

        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        // Add buttons to the stage
        stage.addActor(backButton);
        stage.addActor(level1Button);
        stage.addActor(level2Button);
        stage.addActor(level3Button);
    }

    private void addHoverEffect(ImageButton button, float originalY) {
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.moveTo(button.getX(), originalY + 0.005f * 720, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.moveTo(button.getX(), originalY, 0.1f));
            }
        });
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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (level1Texture != null) level1Texture.dispose();
        if (level2Texture != null) level2Texture.dispose();
        if (level3Texture != null) level3Texture.dispose();
        if (backButtonTexture != null) backButtonTexture.dispose();
        if (backHoverTexture != null) backHoverTexture.dispose();
    }
}
