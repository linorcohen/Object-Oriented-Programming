package src.gameobjects;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * class CameraController.
 * This class describes the Camera controller object.
 */
public class CameraController extends GameObject {

    /**
     * Constance representing the camera factor for width frame
     */
    private static final float FACTOR_WIDTH_FRAME = 1.2f;
    /**
     * Constance representing the max number of balls hits
     */
    private static final int MAX_BALL_HITS = 4;
    /**
     * private field that holds the current ball hits count
     */
    private int curBallCollisionCount;

    private final Ball ball;
    private final WindowController windowController;
    private final GameManager gameManager;

    /**
     * Construct a new CameraController object.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     * @param ball             the main game ball
     * @param gameManager      the game manager
     * @param windowController the game window controller
     */
    public CameraController(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Ball ball,
                            GameManager gameManager, WindowController windowController) {
        super(topLeftCorner, dimensions, renderable);
        this.ball = ball;
        this.gameManager = gameManager;
        this.windowController = windowController;
    }

    /**
     * this method activate the camera if camera isn't already activated.
     */
    public void activateCamera() {
        if (gameManager.getCamera() == null) {
            gameManager.setCamera(new Camera(
                            ball, //object to follow
                            Vector2.ZERO, //follow the center of the object
                            windowController.getWindowDimensions().mult(FACTOR_WIDTH_FRAME),//widen the frame
                            windowController.getWindowDimensions() //share the window dimensions
                    )
            );
        }
    }

    /**
     * this method update the camera status according to the number of ball collisions appeared.
     *
     * @param deltaTime not used
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (gameManager.getCamera() == null) {
            curBallCollisionCount = ball.getCollisionCount();
        } else {
            if (ball.getCollisionCount() - curBallCollisionCount >= MAX_BALL_HITS) {
                gameManager.setCamera(null); //deactivate camera
            }
        }
    }
}
