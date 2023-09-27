package src;

import danogl.collisions.GameObjectCollection;
import src.brick_strategies.*;

/**
 * class StrategyFactory.
 * this class represent a factory for brick strategies
 */
public class StrategyFactory {

    /**
     * this method return a CollisionStrategy according to the given strategy number.
     *
     * @param strategyNum   strategy number to select
     * @param gameObjects   game objects collection
     * @param gameManager   main game manager
     * @param maxStrategies max brick strategies
     * @return new CollisionStrategy instance
     */
    public CollisionStrategy buildStrategy(int strategyNum, GameObjectCollection gameObjects,
                                           BrickerGameManager gameManager, int maxStrategies) {
        switch (strategyNum) {
            case 0:
                return new CameraStrategy(gameObjects, gameManager.gerCameraController());
            case 1:
                return new PuckStrategy(gameObjects, gameManager.getImageReader(),
                        gameManager.getSoundReader());
            case 2:
                return new HeartStrategy(gameObjects, gameManager.getImageReader(),
                        gameManager.getLifeCounter());
            case 3:
                return new TempPaddleStrategy(gameObjects, gameManager.getTempPaddle());
            case 4:
                return new DoubleStrategy(gameObjects, this, maxStrategies - 1, gameManager);
            case 5:
                return new CollisionStrategy(gameObjects);
            default:
                return null;
        }
    }

}
