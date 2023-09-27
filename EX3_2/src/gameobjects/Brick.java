package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;

/**
 * class Brick.
 * This class inherits from GameObject, and describes the brick in the game.
 */
public class Brick extends GameObject {

    private final CollisionStrategy strategy;
    private final Counter counter;

    /**
     * Construct a new Brick instance.
     *
     * @param topLeftCorner the position in the window the top left corner of the object will be placed.
     * @param dimensions    the 2d dimensions of the object on the screen.
     * @param renderable    the image object to display on the screen.
     * @param strategy      the strategy that will be used when the brick breaks.
     * @param counter       the counter of the current bricks
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy strategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.strategy = strategy;
        this.counter = counter;
    }

    /**
     * This is an override method for GameObject's onCollisionEnter. When the game detects a collision
     * between the two objects, it activates the strategy of the brick.
     *
     * @param other     the object this brick has collided with
     * @param collision the attributes of the collision that occurred.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        strategy.onCollision(this, other, counter);
    }
}
