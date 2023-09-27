package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import src.gameobjects.CameraController;

/**
 * class CameraStrategy.
 * this class represent the camera strategy
 */
public class CameraStrategy extends CollisionStrategy {

    /**
     * constance for objects with no tag
     */
    public static final String NO_TAG = "";

    private final CameraController cameraController;

    /**
     * The constructor of the Camera Strategy object.
     *
     * @param gameObjects      An object which holds all game objects of the game running.
     * @param cameraController camera controller object
     */
    public CameraStrategy(GameObjectCollection gameObjects, CameraController cameraController) {
        super(gameObjects);
        this.cameraController = cameraController;
    }

    /**
     * When breaking a brick holding this behavior, the game camera will
     * follow the ball until the ball hits 4 things
     *
     * @param collidedObj   the object that was collided (the brick)
     * @param colliderObj   the object that collided with the brick (the ball).
     * @param bricksCounter the bricks counter
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        if (colliderObj.getTag().equals(NO_TAG)) { // only the main ball can activate the camera
            cameraController.activateCamera();
        }

    }
}



