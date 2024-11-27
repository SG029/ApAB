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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

// New imports for ground creation
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameScreen implements Screen {
    private final AngryBirdGame game;
    private OrthographicCamera camera;
    private PhysicsWorld physicsWorld;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Bird bird;
    private Array<Block> blocks;
    private Array<Pig> pigs;
    private int levelNumber;
    private Texture backgroundTexture;
    private Texture slingshotTexture;
    private Texture pauseButtonTexture;

    // New sound effect
    private Sound collisionSound;

    // Slingshot mechanics (improved)
    private static final float SLINGSHOT_X = 1.3f;
    private static final float SLINGSHOT_Y = 1.2f; // Raised slingshot height
    private static final float SLINGSHOT_WIDTH = 1.5f;
    private static final float SLINGSHOT_HEIGHT = 3f; // Increased slingshot height
    private static final float BIRD_OFFSET_X = 0.5f;
    private static final float BIRD_OFFSET_Y = 0.75f; // Adjusted bird offset

    // Improved launch parameters
    private static final float MAX_PULL_DISTANCE = 3f;
    private static final float LAUNCH_POWER_MULTIPLIER = 15f;
    private static final float ANGLE_CORRECTION_FACTOR = 1.5f;
    private static final float MIN_LAUNCH_FORCE = 5f;
    private static final float MAX_LAUNCH_FORCE = 50f;

    private Vector2 originalBirdPosition;
    private Vector2 currentBirdPosition;
    private Vector2 launchForce = new Vector2();
    private boolean isSlingshotPulled = false;
    private boolean isBirdLaunched = false;

    private static final float PAUSE_BUTTON_X = 0.5f;
    private static final float PAUSE_BUTTON_Y = 7.5f;
    private static final float PAUSE_BUTTON_WIDTH = 1.5f;
    private static final float PAUSE_BUTTON_HEIGHT = 1.5f;

    // New method to create ground
    private void createGround(World world) {
        // Ground body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(8f, 1.2f); // Raised the ground slightly
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        // Create the ground body
        Body groundBody = world.createBody(groundBodyDef);

        // Create a polygon shape for the ground
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(8f, 0.1f); // Width of 16, height of 0.1

        // Create fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.6f;

        // Create the ground fixture
        groundBody.createFixture(fixtureDef);

        // Clean up
        groundShape.dispose();
    }

    public GameScreen(final AngryBirdGame game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9);

        // Initialize physics world with more realistic gravity
        physicsWorld = new PhysicsWorld();
        World world = physicsWorld.getWorld();
        world.setGravity(new Vector2(0, -9.8f)); // Standard gravity

        // Create ground
        createGround(world);

        // Initialize batch and shape renderer
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Load textures
        backgroundTexture = new Texture("game-back.png");
        slingshotTexture = new Texture("slingshot.png");
        pauseButtonTexture = new Texture("pause.png");

        // Load collision sound
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("audio/takar.mp3"));

        // Initialize entities
        blocks = new Array<>();
        pigs = new Array<>();

        // Load level configuration
        LevelManager.loadLevel(levelNumber, world, blocks, pigs);

        // Create bird at slingshot position
        bird = new Bird(world, SLINGSHOT_X + BIRD_OFFSET_X, SLINGSHOT_Y + BIRD_OFFSET_Y);

        // Store initial bird position
        originalBirdPosition = new Vector2(SLINGSHOT_X + BIRD_OFFSET_X, SLINGSHOT_Y + BIRD_OFFSET_Y);
        currentBirdPosition = new Vector2(originalBirdPosition);

        // Add collision listener to detect pig hits
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object userDataA = contact.getFixtureA().getBody().getUserData();
                Object userDataB = contact.getFixtureB().getBody().getUserData();

                // Check if the contact involves a bird and a pig
                if ((userDataA instanceof Bird && userDataB instanceof Pig) ||
                    (userDataA instanceof Pig && userDataB instanceof Bird)) {
                    // Play collision sound
                    collisionSound.play();
                }
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
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

        // Draw bird on the slingshot or in flight
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

        // Draw slingshot bands
        if (isSlingshotPulled && !isBirdLaunched) {
            drawSlingshotBands();
        }
    }

    private void handleInput(float delta) {
        // Touch input for slingshot
        if (Gdx.input.isTouched() && !isBirdLaunched) {
            // Convert touch coordinates to game world coordinates
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Calculate pull distance and angle
            float pullDistance = Vector2.dst(touchPos.x, touchPos.y, originalBirdPosition.x, originalBirdPosition.y);

            if (pullDistance <= MAX_PULL_DISTANCE) {
                isSlingshotPulled = true;

                // Update current bird position
                currentBirdPosition.set(touchPos.x, touchPos.y);

                // Calculate launch vector with improved trajectory calculation
                Vector2 pullVector = new Vector2(originalBirdPosition).sub(currentBirdPosition);

                // Adjust launch power based on pull distance
                float pullStrength = Math.min(pullDistance / MAX_PULL_DISTANCE, 1f);

                // Calculate launch angle with correction
                float launchAngle = (float) Math.atan2(pullVector.y, pullVector.x);
                float launchPower = pullStrength * LAUNCH_POWER_MULTIPLIER;

                // Set launch force with angle correction
                launchForce.x = (float) (Math.cos(launchAngle) * launchPower * ANGLE_CORRECTION_FACTOR);
                launchForce.y = (float) (Math.abs(Math.sin(launchAngle)) * launchPower * ANGLE_CORRECTION_FACTOR);

                // Clamp launch force
                if (launchForce.len() > MAX_LAUNCH_FORCE) {
                    launchForce.scl(MAX_LAUNCH_FORCE / launchForce.len());
                }

                // Update bird body position during pull
                bird.getBody().setTransform(currentBirdPosition.x, currentBirdPosition.y, 0);
                bird.getBody().setLinearVelocity(0, 0);
            }
        }

        // Launch bird when touch is released
        if (!Gdx.input.isTouched() && isSlingshotPulled) {
            if (launchForce.len() > MIN_LAUNCH_FORCE) {
                // Apply impulse with adjusted angle and power
                bird.getBody().applyLinearImpulse(
                    launchForce.x,
                    launchForce.y,
                    bird.getBody().getPosition().x,
                    bird.getBody().getPosition().y,
                    true
                );
                isBirdLaunched = true;
            } else {
                // Return bird to original position if not pulled enough
                bird.getBody().setTransform(originalBirdPosition.x, originalBirdPosition.y, 0);
            }

            isSlingshotPulled = false;
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

    private void drawSlingshotBands() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.5f, 0.3f, 0.1f, 1f);

        // Left slingshot band
        shapeRenderer.rectLine(
            SLINGSHOT_X + 0.2f,
            SLINGSHOT_Y + SLINGSHOT_HEIGHT * 0.7f,
            currentBirdPosition.x,
            currentBirdPosition.y,
            0.1f
        );

        // Right slingshot band
        shapeRenderer.rectLine(
            SLINGSHOT_X + SLINGSHOT_WIDTH - 0.2f,
            SLINGSHOT_Y + SLINGSHOT_HEIGHT * 0.7f,
            currentBirdPosition.x,
            currentBirdPosition.y,
            0.1f
        );

        shapeRenderer.end();
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
        shapeRenderer.dispose();
        backgroundTexture.dispose();
        slingshotTexture.dispose();
        pauseButtonTexture.dispose();
        bird.dispose();
        collisionSound.dispose(); // Dispose of the sound resource
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
