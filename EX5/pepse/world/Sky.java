package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The Sky class is a class for creating the sky in the game
 */
public class Sky {

    /**
     * Constance representing the color of the sky
     */
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    /**
     * Constance representing the sky tag
     */
    private static final String SKY_TAG = "sky";

    /**
     * this method creates the sky object.
     *
     * @param gameObjects      the game objects collection
     * @param windowDimensions a Vector2 object representing the dimensions of the window
     * @param skyLayer         an integer representing the layer that sky object should be added to
     * @return the sky game object
     */
    public static GameObject create(GameObjectCollection gameObjects, Vector2 windowDimensions,
                                    int skyLayer) {

        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        gameObjects.addGameObject(sky, skyLayer);
        return sky;
    }
}
