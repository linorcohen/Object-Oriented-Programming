import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.*;

import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    /**
     * Default border (wall) width
     */
    public static final float BORDER_WIDTH = 700;

    /**
     * Default border (wall) height
     */
    private static final float BORDER_HEIGHT = 500;
    private static final int MIN_DISTANCE_FROM_EDGE = 1;
    private static final int INITIAL_NUM_OF_LIFE = 3;
    private static final int NUM_OF_BRICKS = 56;
    public static final int TARGET_FRAMERATE = 80;
    public static final int BACKROUND_LAYER = -200;
    public static final String GAME_TITLE = "Bricker";
    public static final String YOU_LOST_MASSAGE = "You Lost! Play again?";
    public static final String YOU_WON_MASSAGE = "You Won! Play again?";
    public static final String ASSETS_BRICK_PNG = "assets/brick.png";
    public static final String ASSETS_HEART_PNG = "assets/heart.png";
    public static final String ASSETS_PADDLE_PNG = "assets/paddle.png";
    public static final String ASSETS_BALL_PNG = "assets/ball.png";
    public static final String ASSETS_BALL_SOUND_WAV = "assets/blop_cut_silenced.wav";
    public static final String ASSETS_BACKROUND_JPEG = "assets/DARK_BG2_small.jpeg";
    public static final int NUM_OF_BRICKS_ROWS = 8;
    public static final int NUM_OF_BRICKS_COLS = 7;
    public static final int BRICK_WIDTH = 98;
    public static final int BRICK_HEIGHT = 15;
    public static final Vector2 BRICK_DIMENSIONS_VECTOR = new Vector2(BRICK_WIDTH, BRICK_HEIGHT);
    public static final Vector2 BALL_DIMENSIONS_VECTOR = new Vector2(30, 30);
    public static final Vector2 PADDLE_DIMENSIONS_VECTOR = new Vector2(200, 20);
    public static final Vector2 HEART_DIMENSION_VECTOR = new Vector2(30, 30);
    public static final Vector2 NUMERIC_DIMENSIONS_VECTOR = new Vector2(30, 30);
    public static final int BALL_MOVE_SPEED_X = 200;
    public static final int BALL_MOVE_SPEED_Y = 200;
    public static final int NUMERIC_X_LOCATION = 100;
    public static final int BRICK_X_LOCATION = 7;
    public static final int BRICK_Y_LOCATION = 20;

    private Counter lifeCounter;
    private Ball ball;
    private Counter bricksCounter;
    private final Vector2 windowDimensions;
    private WindowController windowController;
    private UserInputListener inputListener;

    /**
     * This is the constructor of a brick game, which calls its super (GameManager)'s constructor
     *
     * @param windowTitle      the title of the window
     * @param windowDimensions a 2d vector representing the height and width of the window
     */
    BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
    }

    /**
     * This method initializes a new game. It creates all game objects,
     * sets their values and initial positions and allow the start of a game.
     *
     * @param imageReader      an object used to read images from the disc and render them.
     * @param soundReader      an object used to read sound files from the disc and render them.
     * @param inputListener    a listener capable of reading user keyboard inputs
     * @param windowController a controller used to control the window and its attributes
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(TARGET_FRAMERATE);

        this.windowController = windowController;
        this.inputListener = inputListener;
        this.lifeCounter = new Counter(INITIAL_NUM_OF_LIFE);
        this.bricksCounter = new Counter(NUM_OF_BRICKS);

        addBackroundObjectToGame(imageReader);
        addBallObjectToGame(imageReader, soundReader);
        addPaddleToGame(imageReader, inputListener);
        addWallsToGame();
        addBricksToGame(imageReader);
        addGraphicLifeCounterToGame(imageReader);
        addNumericLifeCounterToGame();

    }

    private void addNumericLifeCounterToGame() {
        Vector2 numericTopLeftCorner = new Vector2(NUMERIC_X_LOCATION, windowDimensions.y() - 40);
        gameObjects().addGameObject(new NumericLifeCounter(lifeCounter, numericTopLeftCorner,
                NUMERIC_DIMENSIONS_VECTOR, gameObjects()), BACKROUND_LAYER);
    }

    private void addBricksToGame(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage(ASSETS_BRICK_PNG, true);
        CollisionStrategy strategy = new CollisionStrategy(gameObjects());
        setAllBricksInTheGame(brickImage, strategy);
    }

    private void setAllBricksInTheGame(Renderable brickImage, CollisionStrategy strategy) {
        for (int row = 0; row < NUM_OF_BRICKS_ROWS; row++) {
            Vector2 brickTopLeftCorner = new Vector2(BRICK_X_LOCATION, BRICK_Y_LOCATION + BRICK_HEIGHT * row);
            for (int col = 0; col < NUM_OF_BRICKS_COLS; col++) {
                gameObjects().addGameObject(new Brick(brickTopLeftCorner, BRICK_DIMENSIONS_VECTOR,
                        brickImage, strategy, bricksCounter));
                brickTopLeftCorner = brickTopLeftCorner.add(new Vector2(BRICK_WIDTH, 0));
            }
        }
    }

    private void addGraphicLifeCounterToGame(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage(ASSETS_HEART_PNG, true);
        Vector2 heartTopLeftCorner = new Vector2(MIN_DISTANCE_FROM_EDGE, windowDimensions.y() - 40);
        gameObjects().addGameObject(new GraphicLifeCounter(heartTopLeftCorner, HEART_DIMENSION_VECTOR,
                lifeCounter, heartImage, gameObjects(), INITIAL_NUM_OF_LIFE), BACKROUND_LAYER);
    }

    private void addWallsToGame() {
        addLeftWallToGame();
        addRightWallToGame();
        addUpWallToGame();
    }

    private void addUpWallToGame() {
        GameObject upWall = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), MIN_DISTANCE_FROM_EDGE), null);
        gameObjects().addGameObject(upWall);
    }

    private void addRightWallToGame() {
        GameObject rightWall = new GameObject(Vector2.ZERO,
                new Vector2(MIN_DISTANCE_FROM_EDGE, windowDimensions.y()), null);
        gameObjects().addGameObject(rightWall);
    }

    private void addLeftWallToGame() {
        GameObject leftWall = new GameObject(
                new Vector2(windowDimensions.x() - MIN_DISTANCE_FROM_EDGE, 0),
                new Vector2(MIN_DISTANCE_FROM_EDGE, windowDimensions.y()), null);
        gameObjects().addGameObject(leftWall);
    }

    private void addPaddleToGame(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(ASSETS_PADDLE_PNG, true);
        GameObject paddle = new Paddle(Vector2.ZERO, PADDLE_DIMENSIONS_VECTOR, paddleImage, inputListener,
                windowDimensions, MIN_DISTANCE_FROM_EDGE);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);
    }

    private void addBallObjectToGame(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage(ASSETS_BALL_PNG, true);
        Sound sound = soundReader.readSound(ASSETS_BALL_SOUND_WAV);
        ball = new Ball(Vector2.ZERO, BALL_DIMENSIONS_VECTOR, ballImage, sound);
        ball.setCenter(new Vector2(windowDimensions.mult(0.5F)));

        setBallRandomVelocity();
        gameObjects().addGameObject(ball);
    }

    private void setBallRandomVelocity() {
        float ballVelX = BALL_MOVE_SPEED_X;
        float ballVelY = BALL_MOVE_SPEED_Y;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    private void addBackroundObjectToGame(ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage(ASSETS_BACKROUND_JPEG, true);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        gameObjects().addGameObject(background, BACKROUND_LAYER);
    }

    /**
     * This method overrides the GameManager update method.
     * It checks for game status, and triggers a new game popup.
     *
     * @param deltaTime - used in the super's update method
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        double ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            lifeCounter.decrement();
            ball.setCenter(new Vector2(windowDimensions.mult(0.5F)));
        }

        if (lifeCounter.value() == 0) {
            endOfTheGameDialog(YOU_LOST_MASSAGE);
        } else if (bricksCounter.value() == 0) {
            endOfTheGameDialog(YOU_WON_MASSAGE);
        } else if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            endOfTheGameDialog(YOU_WON_MASSAGE);
        }
    }

    private void endOfTheGameDialog(String prompt) {
        if (windowController.openYesNoDialog(prompt)) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    public static void main(String[] args) {
        new BrickerGameManager(GAME_TITLE, new Vector2(BORDER_WIDTH, BORDER_HEIGHT)).run();
    }
}
