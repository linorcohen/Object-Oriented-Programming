package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * class Ball.
 * This class inherits from GameObject, and describes the ball in the game.
 */
public class Ball extends GameObject {

    private final Sound sound;

    /**
     * Construct a new Ball instance.
     *
     * @param topLeftCorner position of the top left corner of the ball in the window.
     * @param dimensions    the dimensions of the ball
     * @param renderable    the image object of the ball
     * @param sound         the sound file object of the ball's collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound) {
        super(topLeftCorner, dimensions, renderable);
        this.sound = sound;
    }

    /**
     * This method overwrites the OnCollisionEnter of GameObject. When it collides with another object,
     * it flips its direction.
     *
     * @param other     the object that the ball collided with
     * @param collision the collision parameters
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        setVelocity(getVelocity().flipped(collision.getNormal()));
        sound.play();
    }
}
