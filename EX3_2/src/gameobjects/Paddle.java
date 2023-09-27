package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * class Paddle.
 * This class inherits from GameObject, and describes the paddle in the game.
 */
public class Paddle extends GameObject {

    /**
     * The movement speed of the paddle
     */
    private final static int MOVE_SPEED = 1000;

    private final Vector2 dimensions;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int minDistFromEdge;

    /**
     * Construct a new Paddle instance.
     *
     * @param topLeftCorner    the top left corner of the position of the text object
     * @param dimensions       the size of the text object
     * @param renderable       the image file of the paddle
     * @param inputListener    The input listener which waits for user inputs and acts on them.
     * @param windowDimensions The dimensions of the screen, to know the limits for paddle movements.
     * @param minDistFromEdge  Minimum distance allowed for the paddle from the edge of the walls
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.dimensions = dimensions;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
    }

    /**
     * This method is overwritten from GameObject. If right and/or left key is recognised as pressed by the
     * input listener, it moves the paddle, and check that it doesn't move past the borders.
     *
     * @param deltaTime - unused
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;

        boolean isPaddleTouchingTheLeftWall = getTopLeftCorner().x() > minDistFromEdge;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && isPaddleTouchingTheLeftWall) {
            movementDir = Vector2.LEFT;
        }

        boolean isPaddleTouchingTheRightWall = (getTopLeftCorner().x() + dimensions.x()) <
                windowDimensions.x() - minDistFromEdge;
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && isPaddleTouchingTheRightWall) {
            movementDir = Vector2.RIGHT;
        }
        setVelocity(movementDir.mult(MOVE_SPEED));
    }
}
