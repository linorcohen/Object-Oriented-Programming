package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * class Ball.
 * This class inherits from GameObject, and describes the ball in the game.
 */
public class Ball extends GameObject {

    /**
     * constant represent the ball movement speed in the x direction
     */
    private static final int BALL_MOVE_SPEED_X = 200;
    /**
     * constant represent the ball movement speed in the y direction
     */
    private static final int BALL_MOVE_SPEED_Y = 200;

    private final Sound sound;

    private final Counter hitCounter;

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
        setBallRandomVelocity();
        this.hitCounter = new Counter();
    }

    /**
     * private method.
     * this method set the ball velocity to random directions.
     */
    private void setBallRandomVelocity() {
        float ballVelX = BALL_MOVE_SPEED_X;
        float ballVelY = BALL_MOVE_SPEED_Y;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * this method returns the current collision hits the ball made.
     *
     * @return hit counter value
     */
    public int getCollisionCount() {
        return hitCounter.value();
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
        hitCounter.increment();
    }
}
