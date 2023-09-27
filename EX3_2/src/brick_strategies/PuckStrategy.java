package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;

/**
 * class PuckStrategy.
 * this class represent the puck strategy
 */
public class PuckStrategy extends CollisionStrategy {

    /**
     * countenance representing the sound dir fot the puck ball
     */
    private static final String ASSET_PUCK_SOUND_WAV = "assets/blop_cut_silenced.wav";
    /**
     * countenance representing the image dir fot the puck ball
     */
    private static final String ASSET_PUCK_PNG = "assets/mockBall.png";
    /**
     * countenance representing the puck size
     */
    private static final int PUCK_SIZE = 25;
    /**
     * countenance representing the number of puck the strategy creates
     */
    private static final int NUM_OF_PUCK_BALLS = 1;

    private final ImageReader imageReader;
    private final SoundReader soundReader;

    /**
     * The constructor of the Puck Strategy object.
     *
     * @param gameObjects An object which holds all game objects of the game running.
     * @param imageReader an object used to read images from the disc and render them.
     * @param soundReader an object used to read sounds from the disc and render them.
     */
    public PuckStrategy(GameObjectCollection gameObjects, ImageReader imageReader, SoundReader soundReader) {
        super(gameObjects);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * When breaking a brick with the behavior of additional balls, white balls will be created in the
     * center of the brick's position (in its place). their size is a third of the length of the brick and
     * the initial direction of each of them will be a random diagonal.
     *
     * @param collidedObj   the object that was collided (the brick)
     * @param colliderObj   the object that collided with the brick (the ball).
     * @param bricksCounter the bricks counter
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        addPuckBallsToTheGame(collidedObj);
    }

    /**
     * private method.
     * this method add puck balls to the game, in the center of the brick
     * the initial direction of each of them will be a random diagonal.
     *
     * @param collidedObj the object that was collided (the brick)
     */
    private void addPuckBallsToTheGame(GameObject collidedObj) {
        Renderable puckImage = imageReader.readImage(ASSET_PUCK_PNG, true);
        Sound sound = soundReader.readSound(ASSET_PUCK_SOUND_WAV);
        Vector2 puckDimensions = new Vector2(PUCK_SIZE, PUCK_SIZE);
        for (int i = 0; i < NUM_OF_PUCK_BALLS; i++) {
            gameObjects.addGameObject(new Ball(collidedObj.getCenter(), puckDimensions, puckImage, sound));
        }
    }
}
