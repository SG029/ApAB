package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.angrybird.entities.Bird;
import com.angrybird.entities.Block;
import com.angrybird.entities.Pig;
import com.angrybird.levels.LevelManager;
import com.angrybird.physics.PhysicsWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    private final AngryBirdGame game;
    private final int levelNumber;
    private OrthographicCamera camera;
    private PhysicsWorld physicsWorld;
    private SpriteBatch batch;
    private Bird bird;
    private Array<Block> blocks;
    private Array<Pig> pigs;

    /**
     * Constructor for GameScreen.
     *
     * @param game        The main game instance.
     * @param levelNumber The current level number.
     */
    public GameScreen(final AngryBirdGame game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9); // 16:9 aspect ratio

        // Initialize physics world
        physicsWorld = new PhysicsWorld();
        World world = physicsWorld.getWorld();

        // Initialize batch
        batch = new SpriteBatch();

        // Initialize entities
        bird = new Bird(world, 2, 2);
        blocks = new Array<>();
        pigs = new Array<>();

        // Load level configuration
        LevelManager.loadLevel(levelNumber, world, blocks, pigs);
    }

    @Override
    public void render(float delta) {
        // Handle pause input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game, levelNumber));
            return;
        }

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update physics
        physicsWorld.step(delta);

        // Handle input for launching the bird
        if (Gdx.input.justTouched() && !bird.isLaunched()) {
            bird.getBody().applyLinearImpulse(5f, 5f, bird.getBody().getPosition().x, bird.getBody().getPosition().y, true);
            bird.setLaunched(true);
        }

        // Check victory and defeat conditions
        if (pigs.size == 0) {
            game.setScreen(new VictoryScreen(game, levelNumber));
            return;
        } else if (bird.isLaunched() && bird.getBody().getLinearVelocity().len() < 0.1f) {
            game.setScreen(new DefeatScreen(game, levelNumber));
            return;
        }

        // Render entities
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        bird.render(batch);
        for (Block block : blocks) {
            block.render(batch);
        }
        for (Pig pig : pigs) {
            pig.render(batch);
        }
        batch.end();

        // Render physics debug (optional)
        // physicsWorld.renderDebug(camera);
    }

    @Override
    public void resize(int width, int height) {
        // Adjust the viewport
        camera.viewportWidth = 16;
        camera.viewportHeight = 16 * (float) height / width;
        camera.update();
    }

    @Override
    public void show() {
        // Set input processor if needed
    }

    @Override
    public void hide() {
        // Called when this screen is no longer the current screen
    }

    @Override
    public void pause() {
        // Handle pause if necessary
    }

    @Override
    public void resume() {
        // Handle resume if necessary
    }

    @Override
    public void dispose() {
        batch.dispose();
        bird.dispose();
        for (Block block : blocks) {
            block.dispose();
        }
        for (Pig pig : pigs) {
            pig.dispose();
        }
        physicsWorld.dispose();
    }
}
