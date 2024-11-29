package com.angrybird.screens;

import com.angrybird.AngryBirdGame;
import com.angrybird.entities.Bird;
import com.angrybird.entities.Block;
import com.angrybird.entities.Pig;
import com.angrybird.entities.Triangle;
import com.angrybird.entities.Circle;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
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
    private Array<Triangle> triangles;
    private Array<Circle> circles;
    private int levelNumber;
    private Texture backgroundTexture;
    private Texture slingshotTexture;
    private Texture pauseButtonTexture;
    private Sound collisionSound;

    // New game timer variables
    private float gameTimer = 0f;
    private static final float GAME_DURATION_LIMIT = 30f;

    private static final float SLINGSHOT_X = 1.3f;
    private static final float SLINGSHOT_Y = 1.2f;
    private static final float SLINGSHOT_WIDTH = 1.5f;
    private static final float SLINGSHOT_HEIGHT = 3f;
    private static final float BIRD_OFFSET_X = 0.5f;
    private static final float BIRD_OFFSET_Y = 0.75f;

    private static final float MAX_PULL_DISTANCE = 3f;
    private static final float LAUNCH_POWER_MULTIPLIER = 15f;
    private static final float ANGLE_CORRECTION_FACTOR = 1.5f;
    private static final float MIN_LAUNCH_FORCE = 5f;
    private static final float MAX_LAUNCH_FORCE = 50f;

    private static final float PAUSE_BUTTON_X = 0.5f;
    private static final float PAUSE_BUTTON_Y = 7.5f;
    private static final float PAUSE_BUTTON_WIDTH = 1.5f;
    private static final float PAUSE_BUTTON_HEIGHT = 1.5f;

    private Vector2 originalBirdPosition;
    private Vector2 currentBirdPosition;
    private Vector2 launchForce = new Vector2();
    private boolean isSlingshotPulled = false;
    private boolean isBirdLaunched = false;

    public GameScreen(final AngryBirdGame game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9);

        physicsWorld = new PhysicsWorld();
        World world = physicsWorld.getWorld();
        world.setGravity(new Vector2(0, -9.8f));

        createGround(world);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        backgroundTexture = new Texture("game-back.png");
        slingshotTexture = new Texture("slingshot.png");
        pauseButtonTexture = new Texture("pause.png");

        collisionSound = Gdx.audio.newSound(Gdx.files.internal("audio/takar.mp3"));

        blocks = new Array<>();
        pigs = new Array<>();
        triangles = new Array<>();
        circles = new Array<>();

        LevelManager.loadLevel(levelNumber, world, blocks, pigs, triangles, circles);

        bird = new Bird(world, SLINGSHOT_X + BIRD_OFFSET_X, SLINGSHOT_Y + BIRD_OFFSET_Y);

        originalBirdPosition = new Vector2(SLINGSHOT_X + BIRD_OFFSET_X, SLINGSHOT_Y + BIRD_OFFSET_Y);
        currentBirdPosition = new Vector2(originalBirdPosition);

        setupContactListener(world);
    }

    private void setupContactListener(World world) {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object userDataA = contact.getFixtureA().getBody().getUserData();
                Object userDataB = contact.getFixtureB().getBody().getUserData();

                // Check for collision between pig and bird
                if ((userDataA instanceof Bird && userDataB instanceof Pig) ||
                    (userDataA instanceof Pig && userDataB instanceof Bird)) {
                    Bird contactBird = (userDataA instanceof Bird) ? (Bird) userDataA : (Bird) userDataB;
                    Pig contactPig = (userDataA instanceof Pig) ? (Pig) userDataA : (Pig) userDataB;

                    // Get relative velocity of the collision
                    Vector2 velocityA = contact.getFixtureA().getBody().getLinearVelocity();
                    Vector2 velocityB = contact.getFixtureB().getBody().getLinearVelocity();

                    // Calculate relative velocity
                    Vector2 relativeVelocity = new Vector2(
                        Math.abs(velocityA.x - velocityB.x),
                        Math.abs(velocityA.y - velocityB.y)
                    );

                    contactBird.takeDamage(50);
                    contactPig.takeDamage(relativeVelocity);

                    collisionSound.play();
                }

                // Check for collision between pig and ground
                if (userDataA instanceof Pig || userDataB instanceof Pig) {
                    Body groundBody = contact.getFixtureA().getBody();
                    Pig pig = (userDataA instanceof Pig) ? (Pig) userDataA : (Pig) userDataB;

                    // Assuming ground body is static and has a specific y-position (1.2f in your createGround method)
                    if (groundBody.getType() == BodyDef.BodyType.StaticBody &&
                        Math.abs(groundBody.getPosition().y - 1.2f) < 0.1f) {

                        // Get pig's velocity
                        Vector2 pigVelocity = pig.getBody().getLinearVelocity();

                        // Calculate impact velocity
                        Vector2 impactVelocity = new Vector2(
                            Math.abs(pigVelocity.x),
                            Math.abs(pigVelocity.y)
                        );

                        pig.takeDamage(impactVelocity);
                        collisionSound.play();
                    }
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

    private void createGround(World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(8f, 1.2f);
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(8f, 0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.6f;

        groundBody.createFixture(fixtureDef);
        groundShape.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        physicsWorld.step(delta);
        handleInput(delta);
        checkGameConditions(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(backgroundTexture, 0, 0, 16, 9);
        batch.draw(slingshotTexture, SLINGSHOT_X, SLINGSHOT_Y, SLINGSHOT_WIDTH, SLINGSHOT_HEIGHT);

        if (!bird.isDestroyed()) {
            bird.render(batch);
        }

        batch.draw(pauseButtonTexture, PAUSE_BUTTON_X, PAUSE_BUTTON_Y, PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT);

        for (Block block : blocks) {
            block.render(batch);
        }

        for (Pig pig : pigs) {
            pig.render(batch);
        }

        batch.end();

        if (isSlingshotPulled && !isBirdLaunched) {
            drawSlingshotBands();
        }
    }

    private void handleInput(float delta) {
        if (Gdx.input.isTouched() && !isBirdLaunched && !bird.isDestroyed()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            float pullDistance = Vector2.dst(touchPos.x, touchPos.y, originalBirdPosition.x, originalBirdPosition.y);

            if (pullDistance <= MAX_PULL_DISTANCE) {
                isSlingshotPulled = true;
                currentBirdPosition.set(touchPos.x, touchPos.y);

                Vector2 pullVector = new Vector2(originalBirdPosition).sub(currentBirdPosition);
                float pullStrength = Math.min(pullDistance / MAX_PULL_DISTANCE, 1f);

                float launchAngle = (float) Math.atan2(pullVector.y, pullVector.x);
                float launchPower = pullStrength * LAUNCH_POWER_MULTIPLIER;

                launchForce.x = (float) (Math.cos(launchAngle) * launchPower * ANGLE_CORRECTION_FACTOR);
                launchForce.y = (float) (Math.abs(Math.sin(launchAngle)) * launchPower * ANGLE_CORRECTION_FACTOR);

                if (launchForce.len() > MAX_LAUNCH_FORCE) {
                    launchForce.scl(MAX_LAUNCH_FORCE / launchForce.len());
                }

                bird.getBody().setTransform(currentBirdPosition.x, currentBirdPosition.y, 0);
                bird.getBody().setLinearVelocity(0, 0);
            }
        }

        if (!Gdx.input.isTouched() && isSlingshotPulled && !bird.isDestroyed()) {
            if (launchForce.len() > MIN_LAUNCH_FORCE) {
                bird.getBody().applyLinearImpulse(
                    launchForce.x,
                    launchForce.y,
                    bird.getBody().getPosition().x,
                    bird.getBody().getPosition().y,
                    true
                );
                isBirdLaunched = true;
            } else {
                bird.getBody().setTransform(originalBirdPosition.x, originalBirdPosition.y, 0);
            }

            isSlingshotPulled = false;
        }

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (touchPos.x >= PAUSE_BUTTON_X && touchPos.x <= PAUSE_BUTTON_X + PAUSE_BUTTON_WIDTH &&
                touchPos.y >= PAUSE_BUTTON_Y && touchPos.y <= PAUSE_BUTTON_Y + PAUSE_BUTTON_HEIGHT) {
                game.setScreen(new PauseScreen(game, levelNumber));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game, levelNumber));
        }
    }

    private void drawSlingshotBands() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.5f, 0.3f, 0.1f, 1f);

        shapeRenderer.rectLine(
            SLINGSHOT_X + 0.2f,
            SLINGSHOT_Y + SLINGSHOT_HEIGHT * 0.7f,
            currentBirdPosition.x,
            currentBirdPosition.y,
            0.1f
        );

        shapeRenderer.rectLine(
            SLINGSHOT_X + SLINGSHOT_WIDTH - 0.2f,
            SLINGSHOT_Y + SLINGSHOT_HEIGHT * 0.7f,
            currentBirdPosition.x,
            currentBirdPosition.y,
            0.1f
        );

        shapeRenderer.end();
    }

    private void checkGameConditions(float delta) {
        // Track game timer only if bird is launched and not all pigs are destroyed
        if (isBirdLaunched && !pigs.isEmpty()) {
            gameTimer += delta;
        }

        // Check if all pigs are destroyed
        for (int i = pigs.size - 1; i >= 0; i--) {
            if (pigs.get(i).isDestroyed()) {
                Pig pigToRemove = pigs.get(i);
                pigs.removeIndex(i);
                pigToRemove.dispose();
            }
        }

        // Victory condition: All pigs destroyed
        if (pigs.isEmpty()) {
            game.setScreen(new VictoryScreen(game, levelNumber));
            return;
        }

        // Defeat conditions:
        // 1. Bird is destroyed
        if (bird.isDestroyed()) {
            game.setScreen(new DefeatScreen(game, levelNumber));
            return;
        }

        // 2. 30 seconds passed and some pigs remain
        if (gameTimer >= GAME_DURATION_LIMIT && !pigs.isEmpty()) {
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

        if (bird != null) {
            bird.dispose();
        }

        collisionSound.dispose();

        for (Block block : blocks) {
            block.dispose();
        }

        for (Pig pig : pigs) {
            pig.dispose();
        }

        for (Triangle triangle : triangles) {
            triangle.dispose();
        }

        for (Circle circle : circles) {
            circle.dispose();
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
