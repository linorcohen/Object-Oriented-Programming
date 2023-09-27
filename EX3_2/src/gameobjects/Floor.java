package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * class Floor.
 * this class represent te floor of the game
 */
public class Floor extends GameObject {

    private final GameObjectCollection gameObjects;

    /**
     * Construct a new Floor object instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param gameObjects   the game objects collection
     */
    public Floor(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
    }

    /**
     * this method set the floor to collied only with Heart object
     *
     * @param other collider object
     * @return true if the collider was Heart type, else false.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Heart;
    }

    /**
     * this method remove object that collided with the floor from the game.
     *
     * @param other collider object
     */
    @Override
    public void onCollisionExit(GameObject other) {
        super.onCollisionExit(other);
        gameObjects.removeGameObject(other);
    }
}
