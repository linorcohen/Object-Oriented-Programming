package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import src.BrickerGameManager;
import src.StrategyFactory;

import java.util.Random;

/**
 * class DoubleStrategy.
 * this class represent a double strategy.
 */
public class DoubleStrategy extends CollisionStrategy {

    /**
     * private random object
     */
    private final Random rand = new Random();
    /**
     * private array of CollisionStrategy.
     */
    private CollisionStrategy[] strategies;

    private final StrategyFactory strategyFactory;
    private final int numOfDoubles;
    private final BrickerGameManager gameManager;

    /**
     * The constructor of the Double Strategy object.
     *
     * @param gameObjects     An object which holds all game objects of the game running.
     * @param strategyFactory the game strategy factory
     * @param numOfDoubles    the number of doubles left that the brick can hold
     * @param gameManager     the game manager
     */
    public DoubleStrategy(GameObjectCollection gameObjects, StrategyFactory strategyFactory, int numOfDoubles,
                          BrickerGameManager gameManager) {
        super(gameObjects);
        this.strategyFactory = strategyFactory;
        this.numOfDoubles = numOfDoubles;
        this.gameManager = gameManager;
        setStrategiesField();

    }

    /**
     * private method.
     * this method sets two strategies.
     * select a behavior from among the 5 possible behaviors
     * (including another layer of double behavior, and without the "normal" behavior)
     */
    private void setStrategiesField() {
        int idx = 5;
        if (numOfDoubles == 0) {
            idx = 4;
        }
        this.strategies = new CollisionStrategy[]{
                strategyFactory.buildStrategy(rand.nextInt(idx), gameObjects, gameManager,
                        numOfDoubles - 1),
                strategyFactory.buildStrategy(rand.nextInt(idx), gameObjects, gameManager,
                        numOfDoubles - 1)};
    }

    /**
     * A brick with this behavior will activate all the strategies ahe holds.
     *
     * @param collidedObj   the object that was collided (the brick)
     * @param colliderObj   the object that collided with the brick (the ball).
     * @param bricksCounter the bricks counter
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        for (CollisionStrategy strategy : strategies) {
            strategy.onCollision(collidedObj, colliderObj, bricksCounter);
        }
    }
}
