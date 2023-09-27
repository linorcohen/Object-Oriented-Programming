package src;

import danogl.collisions.Layer;
import danogl.GameManager;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * class BrickerGameManager.
 * This class inherits from GameManager, and describes the brick game.
 */
public class BrickerGameManager extends GameManager {

    //game parameters:
    /**
     * constant represent the minimum distance from edge
     */
    private static final int MIN_DISTANCE_FROM_EDGE = 15;
    /**
     * constant represent the initial num of lives
     */
    private static final int INITIAL_NUM_OF_LIVES = 3;
    /**
     * constant represent the number of bricks in the game
     */
    private static final int NUM_OF_BRICKS = 56;
    /**
     * constant represent the target framerate
     */
    private static final int TARGET_FRAMERATE = 80;
    /**
     * constant represent the window title
     */
    private static final String WINDOW_TITLE = "Bricker";
    /**
     * constant represent the object distunce from the ceiling
     */
    private static final int OBJ_DISTUNCE_FROM_CEILING = 20;
    /**
     * constant represent the number of strategies
     */
    private static final int NUM_OF_STRATEGIES = 6;
    /**
     * constant represent the number of rows of bricks
     */
    private static final int NUM_OF_BRICKS_ROWS = 8;
    /**
     * constant represent the number of columns of bricks
     */
    private static final int NUM_OF_BRICKS_COLS = 7;
    /**
     * constant represent the numeric x direction initial location
     */
    private static final int NUMERIC_X_LOCATION = 90;
    /**
     * constant represent the brick x direction initial location
     */
    private static final int BRICK_X_LOCATION = 7;
    /**
     * constant represent the brick y direction initial location
     */
    private static final int BRICK_Y_LOCATION = 40;
    /**
     * constant represent the max number of strategies a brick can hold
     */
    private static final int MAX_BRICK_STRATEGIES = 3;

    //game objects massages:
    /**
     * constant represent the massage if the player lost
     */
    private static final String YOU_LOST_MASSAGE = "You Lost! Play again?";
    /**
     * constant represent the massage if the player won
     */
    private static final String YOU_WON_MASSAGE = "You Won! Play again?";

    //game objects assets:
    /**
     * constant represent the brick image path
     */
    private static final String ASSET_BRICK_PNG = "assets/brick.png";
    /**
     * constant represent the heart image path
     */
    private static final String ASSET_HEART_PNG = "assets/heart.png";
    /**
     * constant represent the paddle image path
     */
    private static final String ASSET_PADDLE_PNG = "assets/paddle.png";
    /**
     * constant represent the ball image path
     */
    private static final String ASSET_BALL_PNG = "assets/ball.png";
    /**
     * constant represent the ball sound path
     */
    private static final String ASSET_BALL_SOUND_WAV = "assets/blop_cut_silenced.wav";
    /**
     * constant represent the backround image path
     */
    private static final String ASSET_BACKROUND_JPEG = "assets/DARK_BG2_small.jpeg";
    /**
     * constant represent the temp paddle image path
     */
    private static final String ASSETS_TEMP_PADDLE_PNG = "assets/botGood.png";

    //game objects sizes:
    /**
     * Default border (wall) width
     */
    private static final float BORDER_WIDTH = 4.0F;
    /**
     * constant represent the brick width
     */
    private static final int BRICK_WIDTH = 98;
    /**
     * constant represent the brick height
     */
    private static final int BRICK_HEIGHT = 15;
    /**
     * constant represent the ball width
     */
    private static final int BALL_SIZE = 22;
    /**
     * constant represent the paddle width
     */
    private static final int PADDLE_WIDTH = 110;
    /**
     * constant represent the paddle height
     */
    private static final int PADDLE_HEIGHT = 15;
    /**
     * constant represent the numeric width
     */
    private static final int NUMERIC_WIDTH = 20;
    /**
     * constant represent the numeric height
     */
    private static final int NUMERIC_HEIGHT = 20;
    /**
     * constant represent the heart width
     */
    private static final int HEART_WIDTH = 20;
    /**
     * constant represent the heart height
     */
    private static final int HEART_HEIGHT = 20;
    /**
     * constant represent the window width
     */
    private static final int WINDOW_WIDTH = 700;
    /**
     * constant represent the window height
     */
    private static final int WINDOW_HEIGHT = 500;
    /**
     * constant represent the floor height
     */
    private static final int FLOOR_HEIGHT = 10;

    // game factors:
    /**
     * constant represent the value 0.5f
     */
    private static final float FACTOR_FOR_MULTIPLY_BY_HALF = 0.5F;
    /**
     * constant represent the number 2
     */
    private static final int FACTOR_TWO = 2;

    //game private fields:
    /**
     * private counter for the current lives left in the game
     */
    private Counter lifeCounter;
    /**
     * private ball object of the game
     */
    private Ball ball;
    /**
     * private counter for the current bricks left in the game
     */
    private Counter bricksCounter;
    /**
     * private window controller object
     */
    private WindowController windowController;
    /**
     * private UserInputListener object
     */
    private UserInputListener inputListener;
    /**
     * private ImageReader object
     */
    private ImageReader imageReader;
    /**
     * private Vector represent the windows Dimensions
     */
    private final Vector2 windowDimensions;
    /**
     * private TempPaddle object
     */
    private TempPaddle tempPaddle;
    /**
     * private CameraController object
     */
    private CameraController cameraController;
    /**
     * private SoundReader object
     */
    private SoundReader soundReader;


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
     * getter for ball object
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * getter for TempPaddle object
     */
    public TempPaddle getTempPaddle() {
        return tempPaddle;
    }

    /**
     * getter for CameraController object
     */
    public CameraController gerCameraController() {
        return cameraController;
    }

    /**
     * getter for LifeCounter object
     */
    public Counter getLifeCounter() {
        return lifeCounter;
    }

    /**
     * getter for ImageReader object
     */
    public ImageReader getImageReader() {
        return imageReader;
    }

    /**
     * getter for SoundReader object
     */
    public SoundReader getSoundReader() {
        return soundReader;
    }


    /**
     * This method initializes a new game. It creates all game objects,
     * sets their values and initial positiallons and allow the start of a game.
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

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.lifeCounter = new Counter(INITIAL_NUM_OF_LIVES);
        this.bricksCounter = new Counter(NUM_OF_BRICKS);

        addBackroundObjectToGame();
        addWallsToGame();
        addBallObjectToGame();
        addCameraCounterToGame();
        addFloorToTheGame();
        addPaddlesToGame();
        addBricksToGame();
        addGraphicLifeCounterToGame();
        addNumericLifeCounterToGame();

    }

    /**
     * private method.
     * this method add backround to the game.
     */
    private void addBackroundObjectToGame() {
        Renderable backgroundImage = imageReader.readImage(ASSET_BACKROUND_JPEG, false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * private method.
     * this method add invisible walls objects to the game.
     */
    private void addWallsToGame() {
        addLeftWallToGame();
        addRightWallToGame();
        addUpWallToGame();
    }

    /**
     * private method.
     * this method add invisible up wall object to the game.
     */
    private void addUpWallToGame() {
        GameObject upWall = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_WIDTH), null);
        gameObjects().addGameObject(upWall, Layer.STATIC_OBJECTS);
    }

    /**
     * private method.
     * this method add right wall object to the game.
     */
    private void addRightWallToGame() {
        GameObject rightWall = new GameObject(Vector2.ZERO,
                new Vector2(BORDER_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);
    }

    /**
     * private method.
     * this method add left wall object to the game.
     */
    private void addLeftWallToGame() {
        GameObject leftWall = new GameObject(
                new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                new Vector2(MIN_DISTANCE_FROM_EDGE, windowDimensions.y()), null);
        gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);
    }

    /**
     * private method.
     * this method add a ball to the game.
     */
    private void addBallObjectToGame() {
        Vector2 ballDimensionsVector = new Vector2(BALL_SIZE, BALL_SIZE);
        Renderable ballImage = imageReader.readImage(ASSET_BALL_PNG, true);
        Sound sound = soundReader.readSound(ASSET_BALL_SOUND_WAV);
        ball = new Ball(Vector2.ZERO, ballDimensionsVector, ballImage, sound);
        ball.setCenter(new Vector2(windowDimensions.mult(FACTOR_FOR_MULTIPLY_BY_HALF)));
        ball.setTag("mainBall");
        gameObjects().addGameObject(ball);
    }

    /**
     * private method.
     * this method add the camera controller object to the game.
     */
    private void addCameraCounterToGame() {
        cameraController = new CameraController(Vector2.ZERO, Vector2.ZERO, null, ball,
                this, windowController);
        gameObjects().addGameObject(cameraController);
    }

    /**
     * private method.
     * this method add the floor object to the game.
     */
    private void addFloorToTheGame() {
        gameObjects().addGameObject(new Floor(new Vector2(0, WINDOW_HEIGHT - FLOOR_HEIGHT),
                new Vector2(WINDOW_WIDTH, FLOOR_HEIGHT), null, gameObjects()));
    }

    /**
     * private method.
     * this method add paddle object to the game.
     */
    private void addPaddlesToGame() {
        Vector2 paddleDimensions = new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT);
        addPaddleToGame(paddleDimensions);
        addTempPaddleToGame(paddleDimensions);
    }

    /**
     * private method.
     * this method adds the paddle to the game.
     *
     * @param paddleDimensions paddle dimensions vector
     */
    private void addPaddleToGame(Vector2 paddleDimensions) {
        Renderable paddleImage = imageReader.readImage(ASSET_PADDLE_PNG, true);
        GameObject paddle = new Paddle(Vector2.ZERO, paddleDimensions, paddleImage, inputListener,
                windowDimensions, MIN_DISTANCE_FROM_EDGE);
        paddle.setCenter(new Vector2(windowDimensions.x() / FACTOR_TWO,
                windowDimensions.y() - OBJ_DISTUNCE_FROM_CEILING));
        gameObjects().addGameObject(paddle);
    }

    /**
     * private method.
     * this method adds the paddle to the game.
     *
     * @param tempPaddleDimensions temp paddle dimensions vector
     */
    private void addTempPaddleToGame(Vector2 tempPaddleDimensions) {
        Renderable tempPaddleImage = imageReader.readImage(ASSETS_TEMP_PADDLE_PNG, true);
        TempPaddle tempPaddle = new TempPaddle(windowDimensions.mult(FACTOR_FOR_MULTIPLY_BY_HALF),
                tempPaddleDimensions, tempPaddleImage, inputListener,
                windowDimensions, MIN_DISTANCE_FROM_EDGE);
        gameObjects().addGameObject(tempPaddle);
        this.tempPaddle = tempPaddle;
    }

    /**
     * private method.
     * this method add bricks objects to the game.
     */
    private void addBricksToGame() {

        Renderable brickImage = imageReader.readImage(ASSET_BRICK_PNG, true);
        Vector2 brickDimensions = new Vector2(BRICK_WIDTH, BRICK_HEIGHT);

        StrategyFactory strategyFactory = new StrategyFactory();
        setAllBricksInTheGame(brickImage, brickDimensions, strategyFactory);
    }

    /**
     * private method.
     * this method add all the bricks to the game according to the NUM_OF_BRICKS_ROWS and NUM_OF_BRICKS_COLS.
     *
     * @param brickImage the image object to display on the screen.
     */
    private void setAllBricksInTheGame(Renderable brickImage, Vector2 brickDimensions,
                                       StrategyFactory strategyFactory) {
        Random rand = new Random();
        int strategyIndex;
        for (int row = 0; row < NUM_OF_BRICKS_ROWS; row++) {
            Vector2 brickTopLeftCorner = new Vector2(BRICK_X_LOCATION, BRICK_Y_LOCATION +
                    BRICK_HEIGHT * row);

            for (int col = 0; col < NUM_OF_BRICKS_COLS; col++) {

                strategyIndex = rand.nextInt(NUM_OF_STRATEGIES);

                gameObjects().addGameObject(new Brick(brickTopLeftCorner, brickDimensions, brickImage,
                        strategyFactory.buildStrategy(strategyIndex, gameObjects(), this,
                                MAX_BRICK_STRATEGIES), bricksCounter));
                brickTopLeftCorner = brickTopLeftCorner.add(new Vector2(BRICK_WIDTH, 0));
            }
        }
    }

    /**
     * private method.
     * this method add GraphicLifeCounter object to the game.
     */
    private void addGraphicLifeCounterToGame() {
        Vector2 heartDimension = new Vector2(HEART_WIDTH, HEART_HEIGHT);
        Renderable heartImage = imageReader.readImage(ASSET_HEART_PNG, true);
        Vector2 heartTopLeftCorner = new Vector2(0, windowDimensions.y() - OBJ_DISTUNCE_FROM_CEILING);
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(heartTopLeftCorner, heartDimension,
                lifeCounter, heartImage, gameObjects(), INITIAL_NUM_OF_LIVES);
        graphicLifeCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(graphicLifeCounter, Layer.UI);
    }

    /**
     * private method.
     * this method add NumericLifeCounter object to the game.
     */
    private void addNumericLifeCounterToGame() {
        Vector2 numericDimensionsVector = new Vector2(NUMERIC_WIDTH, NUMERIC_HEIGHT);
        Vector2 numericTopLeftCorner = new Vector2(NUMERIC_X_LOCATION,
                windowDimensions.y() - OBJ_DISTUNCE_FROM_CEILING);
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(lifeCounter, numericTopLeftCorner,
                numericDimensionsVector, gameObjects());
        numericLifeCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(numericLifeCounter, Layer.UI);
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
            ball.setCenter(new Vector2(windowDimensions.mult(FACTOR_FOR_MULTIPLY_BY_HALF)));
        }

        if (lifeCounter.value() == 0) {
            endOfTheGameDialog(YOU_LOST_MASSAGE);
        } else if (bricksCounter.value() <= 0) {
            endOfTheGameDialog(YOU_WON_MASSAGE);
        } else if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            endOfTheGameDialog(YOU_WON_MASSAGE);
        }
    }

    /**
     * private method.
     * this method opens a yes or no dialog for the user with the given prompt.
     * in case of yes from the user the game rest, else the window closes.
     *
     * @param informativeMessage informative message of the user status
     */
    private void endOfTheGameDialog(String informativeMessage) {
        if (windowController.openYesNoDialog(informativeMessage)) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    public static void main(String[] args) {
        new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
    }
}
