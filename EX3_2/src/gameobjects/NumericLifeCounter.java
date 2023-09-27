package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * class NumericLifeCounter.
 * This class inherits from GameObject, and describes the textual disqualification counter in the game.
 */
public class NumericLifeCounter extends GameObject {

    /**
     * constance array of text colors enum
     */
    private final Color[] colors = {Color.RED, Color.yellow, Color.green, Color.green};

    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;

    /**
     * the Text renderable of the number counter
     */
    private final TextRenderable textRenderable;

    /**
     * Construct a new NumericLifeCounter instance.
     *
     * @param livesCounter         The counter of how many lives are left right now.
     * @param topLeftCorner        the top left corner of the position of the text object
     * @param dimensions           the size of the text object
     * @param gameObjectCollection the collection of all game objects currently in the game
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.textRenderable = new TextRenderable(Integer.toString(livesCounter.value()));
        this.renderer().setRenderable(textRenderable);
    }

    /**
     * This method is overwritten from GameObject. It sets the string value of the text object to the number
     * of current lives left.
     *
     * @param deltaTime unused
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        textRenderable.setColor(colors[livesCounter.value() - 1]);
        textRenderable.setString(Integer.toString(livesCounter.value()));
    }
}
