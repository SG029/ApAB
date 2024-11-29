package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class VictoryScreen implements Screen {
    private final AngryBirdGame game;
    private int levelNumber;
    private SpriteBatch batch;

    private Texture backgroundTexture;
    private Texture nextButtonTexture;
    private Texture retryButtonTexture;
    private Texture menuButtonTexture;
    private Texture exitButtonTexture;

    private static final float BUTTON_WIDTH = 2f;
    private static final float BUTTON_HEIGHT = 1f;
    private static final float BUTTON_PADDING = 0.5f;

    public VictoryScreen(final AngryBirdGame game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;

        batch = new SpriteBatch();

        backgroundTexture = new Texture("victory-screen.png");
        nextButtonTexture = new Texture("nxt-button.png");
        retryButtonTexture = new Texture("retry-btn.png");
        menuButtonTexture = new Texture("menu-btn.png");
        exitButtonTexture = new Texture("exit-btn.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(backgroundTexture, 0, 0, 16, 9);

        float nextButtonX = 4f;
        float nextButtonY = 2f;
        batch.draw(nextButtonTexture, nextButtonX, nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        float retryButtonX = nextButtonX;
        float retryButtonY = nextButtonY - BUTTON_HEIGHT - BUTTON_PADDING;
        batch.draw(retryButtonTexture, retryButtonX, retryButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        float menuButtonX = nextButtonX;
        float menuButtonY = retryButtonY - BUTTON_HEIGHT - BUTTON_PADDING;
        batch.draw(menuButtonTexture, menuButtonX, menuButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        float exitButtonX = nextButtonX;
        float exitButtonY = menuButtonY - BUTTON_HEIGHT - BUTTON_PADDING;
        batch.draw(exitButtonTexture, exitButtonX, exitButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touchPos);

            float nextButtonX = 4f;
            float nextButtonY = 2f;

            if (touchPos.x >= nextButtonX && touchPos.x <= nextButtonX + BUTTON_WIDTH &&
                touchPos.y >= nextButtonY && touchPos.y <= nextButtonY + BUTTON_HEIGHT) {

                game.getGameStateManager().addCompletedLevel(levelNumber);

                if (levelNumber < 3) {
                    game.setScreen(new GameScreen(game, levelNumber + 1));
                } else {
                    game.setScreen(new HomeScreen(game));
                }
            }

            float retryButtonX = nextButtonX;
            float retryButtonY = nextButtonY - BUTTON_HEIGHT - BUTTON_PADDING;
            if (touchPos.x >= retryButtonX && touchPos.x <= retryButtonX + BUTTON_WIDTH &&
                touchPos.y >= retryButtonY && touchPos.y <= retryButtonY + BUTTON_HEIGHT) {
                game.setScreen(new GameScreen(game, levelNumber));
            }

            float menuButtonX = nextButtonX;
            float menuButtonY = retryButtonY - BUTTON_HEIGHT - BUTTON_PADDING;
            if (touchPos.x >= menuButtonX && touchPos.x <= menuButtonX + BUTTON_WIDTH &&
                touchPos.y >= menuButtonY && touchPos.y <= menuButtonY + BUTTON_HEIGHT) {
                game.setScreen(new HomeScreen(game));
            }

            float exitButtonX = nextButtonX;
            float exitButtonY = menuButtonY - BUTTON_HEIGHT - BUTTON_PADDING;
            if (touchPos.x >= exitButtonX && touchPos.x <= exitButtonX + BUTTON_WIDTH &&
                touchPos.y >= exitButtonY && touchPos.y <= exitButtonY + BUTTON_HEIGHT) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        nextButtonTexture.dispose();
        retryButtonTexture.dispose();
        menuButtonTexture.dispose();
        exitButtonTexture.dispose();
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
