package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Heart;

/**
 * class HeartStrategy.
 * this class represent the Heart Strategy
 */
public class HeartStrategy extends CollisionStrategy {

    /**
     * constant represent the heart width
     */
    private static final int HEART_WIDTH = 20;
    /**
     * constant represent the heart height
     */
    private static final int HEART_HEIGHT = 20;
    /**
     * Constance represent the heart falling velocity in y direction
     */
    private static final int HEART_Y_VELOCITY = 100;
    /**
     * Constance represent the heart image dir
     */
    private static final String ASSETS_HEART_PNG = "assets/heart.png";
    /**
     * Heart object of the strategy
     */
    private Heart fallingHeart;

    private final ImageReader imageReader;
    private final Counter lifeCounter;

    /**
     * The constructor of the Heart Strategy object.
     *
     * @param gameObjects An object which holds all game objects of the game running.
     * @param imageReader an object used to read images from the disc and render them.
     * @param lifeCounter life counter of the game
     */
    public HeartStrategy(GameObjectCollection gameObjects, ImageReader imageReader, Counter lifeCounter) {
        super(gameObjects);
        this.imageReader = imageReader;
        this.lifeCounter = lifeCounter;
        setFallingHeartObject();
    }

    /**
     * private method.
     * this method sets the Strategy falling heart object.
     */
    private void setFallingHeartObject() {
        Renderable fallingHeartImage = imageReader.readImage(ASSETS_HEART_PNG, true);
        Vector2 heartDimensions = new Vector2(HEART_WIDTH, HEART_HEIGHT);
        this.fallingHeart = new Heart(Vector2.ZERO, heartDimensions, fallingHeartImage, lifeCounter);
    }

    /**
     * When breaking a brick with this behavior, a heart object will fall from the center of the brick,
     * which the paddle must "pick up" in order to return disqualification
     *
     * @param collidedObj   the object that was collided (the brick)
     * @param colliderObj   the object that collided with the brick (the ball).
     * @param bricksCounter the bricks counter
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        fallingHeart.setCenter(collidedObj.getCenter());
        fallingHeart.setVelocity(new Vector2(0, HEART_Y_VELOCITY));
        gameObjects.addGameObject(fallingHeart);
    }
}
