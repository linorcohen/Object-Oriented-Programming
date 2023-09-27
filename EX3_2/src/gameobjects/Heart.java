package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * class Heart.
 * this class represent Heart object.
 */
public class Heart extends GameObject {

    /**
     * constance representing the max life value
     */
    private static final int MAX_LIFE_VALUE = 4;

    private final Counter lifeCounter;

    /**
     * Construct a new Heart object instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param lifeCounter   the game life counter
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Counter lifeCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.lifeCounter = lifeCounter;
    }

    /**
     * this method set the heart to collied only with Paddle object (not TempPaddle)
     *
     * @param other collider object
     * @return true if the collider was Paddle type, else false.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Paddle || other instanceof Floor;
    }

    /**
     * this method increment the life counter if heart collided with paddle.
     *
     * @param other     collider object
     * @param collision the attributes of the collision that occurred.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Paddle) {
            if (lifeCounter.value() < MAX_LIFE_VALUE) {
                lifeCounter.increment();
            }
            setDimensions(Vector2.ZERO);
        }
    }
}
