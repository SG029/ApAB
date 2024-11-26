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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    private final AngryBirdGame game;
    private OrthographicCamera camera;
    private PhysicsWorld physicsWorld;
    private SpriteBatch batch;
    private Bird bird;
    private Array<Block> blocks;
    private Array<Pig> pigs;
    private int levelNumber;
    private Texture backgroundTexture;
    private Texture slingshotTexture;
    private Texture pauseButtonTexture;

    // Pause button positioning
    private static final float PAUSE_BUTTON_X = 0.5f;
    private static final float PAUSE_BUTTON_Y = 7.5f;
    private static final float PAUSE_BUTTON_WIDTH = 1.5f;
    private static final float PAUSE_BUTTON_HEIGHT = 1.5f;

    // Slingshot positioning
    private static final float SLINGSHOT_X = 1.3f;
    private static final float SLINGSHOT_Y = 1.2f;
    private static final float SLINGSHOT_WIDTH = 1.5f;
    private static final float SLINGSHOT_HEIGHT = 2f;
    private static final float BIRD_OFFSET_X = 0.5f;
    private static final float BIRD_OFFSET_Y = 0.5f;

    // Bird launch constants
    private static final float MAX_LAUNCH_FORCE = 20f;
    private static final float LAUNCH_FORCE_SCALE = 0.01f;

    private Vector2 launchForce = new Vector2();
    private boolean isBirdLaunched = false;

    public GameScreen(final AngryBirdGame game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9);

        // Initialize physics world
        physicsWorld = new PhysicsWorld();
        World world = physicsWorld.getWorld();

        // Initialize batch
        batch = new SpriteBatch();

        // Load textures
        backgroundTexture = new Texture("game-back.png");
        slingshotTexture = new Texture("slingshot.png");
        pauseButtonTexture = new Texture("pause.png");

        // Initialize entities
        blocks = new Array<>();
        pigs = new Array<>();

        // Load level configuration
        LevelManager.loadLevel(levelNumber, world, blocks, pigs);

        // Create bird at slingshot position
        bird = new Bird(world, SLINGSHOT_X + BIRD_OFFSET_X, SLINGSHOT_Y + BIRD_OFFSET_Y);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update physics
        physicsWorld.step(delta);

        // Handle input for bird launch and pause button
        handleInput(delta);

        // Check game conditions
        checkGameConditions();

        // Render
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, 0, 0, 16, 9);

        // Draw slingshot
        batch.draw(slingshotTexture, SLINGSHOT_X, SLINGSHOT_Y, SLINGSHOT_WIDTH, SLINGSHOT_HEIGHT);

        // Draw bird on the slingshot
        bird.render(batch);

        // Draw pause button
        batch.draw(pauseButtonTexture, PAUSE_BUTTON_X, PAUSE_BUTTON_Y, PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT);

        // Render other game entities
        for (Block block : blocks) {
            block.render(batch);
        }
        for (Pig pig : pigs) {
            pig.render(batch);
        }
        batch.end();
    }

    private void handleInput(float delta) {
        // Handle bird launch
        if (Gdx.input.isTouched() && !isBirdLaunched) {
            // Calculate launch force based on touch position
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            launchForce.x = (touchPos.x - (SLINGSHOT_X + BIRD_OFFSET_X)) * LAUNCH_FORCE_SCALE;
            launchForce.y = (touchPos.y - (SLINGSHOT_Y + BIRD_OFFSET_Y)) * LAUNCH_FORCE_SCALE;

            // Clamp the launch force to the maximum value
            float force = launchForce.len();
            if (force > MAX_LAUNCH_FORCE) {
                launchForce.scl(MAX_LAUNCH_FORCE / force);
            }
        }

        // Launch the bird when the touch is released
        if (Gdx.input.justTouched() && !isBirdLaunched) {
            bird.getBody().applyLinearImpulse(launchForce.x, launchForce.y,
                bird.getBody().getPosition().x,
                bird.getBody().getPosition().y,
                true);
            isBirdLaunched = true;
        }

        // Handle pause button click
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Check if pause button is clicked
            if (touchPos.x >= PAUSE_BUTTON_X && touchPos.x <= PAUSE_BUTTON_X + PAUSE_BUTTON_WIDTH &&
                touchPos.y >= PAUSE_BUTTON_Y && touchPos.y <= PAUSE_BUTTON_Y + PAUSE_BUTTON_HEIGHT) {
                // Open pause screen
                game.setScreen(new PauseScreen(game, levelNumber));
            }
        }

        // Optional: Allow pausing with Escape key
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game, levelNumber));
        }
    }

    private void checkGameConditions() {
        // Victory condition: All pigs destroyed
        if (pigs.isEmpty()) {
            game.setScreen(new VictoryScreen(game, levelNumber));
        }
        // Defeat condition: Bird launched but stopped moving
        else if (isBirdLaunched && bird.getBody().getLinearVelocity().len() < 0.1f) {
            game.setScreen(new DefeatScreen(game, levelNumber));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        slingshotTexture.dispose();
        pauseButtonTexture.dispose();
        bird.dispose();
        for (Block block : blocks) {
            block.dispose();
        }
        for (Pig pig : pigs) {
            pig.dispose();
        }
        physicsWorld.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 16;
        camera.viewportHeight = 16 * (float) height / width;
        camera.update();
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
