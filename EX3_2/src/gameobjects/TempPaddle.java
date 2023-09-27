package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * class TempPaddle
 * this class represent a temp paddle object
 */
public class TempPaddle extends Paddle {

    /**
     * constance represent the max hits on the temp paddle
     */
    private static final int MAX_HITS_IN_TEMP_PADDLE = 3;
    /**
     * private field that holds the current temp activation status
     */
    private boolean tempPaddleActivate;

    private final Counter hitCounter;
    private final Vector2 paddleDimensions;


    /**
     * Construct a new Temp Paddle object instance.
     *
     * @param topLeftCorner    the top left corner of the position of the text object
     * @param dimensions       the size of the text object
     * @param renderable       the image file of the paddle
     * @param inputListener    The input listener which waits for user inputs and acts on them.
     * @param windowDimensions The dimensions of the screen, to know the limits for paddle movements.
     * @param minDistFromEdge  Minimum distance allowed for the paddle from the edge of the walls
     */
    public TempPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener
            inputListener, Vector2 windowDimensions, int minDistFromEdge) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);
        this.hitCounter = new Counter();
        this.paddleDimensions = dimensions;
        deactivateTempPaddle();
    }

    /**
     * this method set the tempPaddle to collied only with the Ball object
     *
     * @param other collider object
     * @return true if other is Ball object, else false.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Ball;
    }

    /**
     * this method deactivate the temp Paddle after 3 hits in it.
     *
     * @param other collider object
     */
    @Override
    public void onCollisionExit(GameObject other) {
        super.onCollisionExit(other);
        if (tempPaddleActivate) {
            hitCounter.decrement();
            if (hitCounter.value() == 0) {
                deactivateTempPaddle();
            }
        }
    }

    /**
     * private method.
     * this method deactivate the temp paddle by setting its size to 0x0.
     * and set the activation status to false.
     */
    private void deactivateTempPaddle() {
        setDimensions(Vector2.ZERO);
        tempPaddleActivate = false;
    }

    /**
     * getter for temp paddle activation status
     *
     * @return temp Paddle Activate status
     */
    public boolean getActivateStatus() {
        return tempPaddleActivate;
    }

    /**
     * this method activate the temp paddle by setting its status to true,
     * reset hit counter, and set to paddle dimensions.
     */
    public void activateTempPaddle() {
        tempPaddleActivate = true;
        setDimensions(paddleDimensions);
        hitCounter.increaseBy(MAX_HITS_IN_TEMP_PADDLE);
    }
}
