package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import src.gameobjects.TempPaddle;

/**
 * class TempPaddleStrategy.
 * This class describes the temp paddle strategy in the game.
 */
public class TempPaddleStrategy extends CollisionStrategy {

    private final TempPaddle tempPaddle;

    /**
     * The constructor of the Temp Strategy object.
     *
     * @param gameObjects An object which holds all game objects of the game running.
     */
    public TempPaddleStrategy(GameObjectCollection gameObjects, TempPaddle tempPaddle) {
        super(gameObjects);
        this.tempPaddle = tempPaddle;
    }

    /**
     * When breaking a brick with additional disk behavior, a temp paddle will be added the same size as the
     * normal paddle. It will be set In the center of the X-axis of the screen, and at the height of
     * half of the screen. The additional temp paddle will also move according to the player's movements,
     * similar to the original paddle.
     *
     * @param collidedObj   the object that was collided (the brick)
     * @param colliderObj   the object that collided with the brick (the ball).
     * @param bricksCounter the bricks counter
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        if (!tempPaddle.getActivateStatus()) {
            tempPaddle.activateTempPaddle();
        }
    }
}
