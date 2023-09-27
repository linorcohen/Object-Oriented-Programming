package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

//TODO - "It creates a 0x0 sized object"

/**
 * class GraphicLifeCounter.
 * This class inherits from GameObject, and describes the heart
 * (the amount of graphical disqualification) in the game.
 */
public class GraphicLifeCounter extends GameObject {

    private static final int BACKGROUND_LAYER = -200;

    private final Counter livesCounter;
    private final GameObjectCollection gameObjectsCollection;
    private int numOfLives;

    /**
     * GameObject array contain all hearts objects in the game.
     */
    private GameObject[] hearts;

    /**
     * Construct a new GraphicLifeCounter instance.
     *
     * @param widgetTopLeftCorner   the top left corner of the left most heart
     * @param widgetDimensions      the dimension of each heart
     * @param livesCounter          the counter which holds current lives count
     * @param widgetRenderable      the image renderable of the hearts
     * @param gameObjectsCollection the collection of all game objects currently in the game
     * @param numOfLives            number of current lives
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable, GameObjectCollection gameObjectsCollection,
                              int numOfLives) {

        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.livesCounter = livesCounter;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
        this.hearts = new GameObject[numOfLives];

        addHeartsToGameObjects(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
    }

    /**
     * This method creates numOfLives hearts, and adds them to the game.
     *
     * @param widgetTopLeftCorner the top left corner of the left most heart
     * @param widgetDimensions    the dimension of each heart
     * @param widgetRenderable    the image renderable of the hearts
     */
    private void addHeartsToGameObjects(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions,
                                        Renderable widgetRenderable) {
        for (int i = 0; i < numOfLives; i++) {
            hearts[i] = new GameObject(widgetTopLeftCorner.add(
                    new Vector2(i * widgetDimensions.x(), 0)),
                    widgetDimensions, widgetRenderable);
            gameObjectsCollection.addGameObject(hearts[i], BACKGROUND_LAYER);
        }
    }

    /**
     * This method is overwritten from GameObject It removes hearts from the screen if there are
     * more hearts than there are lives left
     *
     * @param deltaTime unused
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() < numOfLives) {
            numOfLives--;
            gameObjectsCollection.removeGameObject(hearts[numOfLives], BACKGROUND_LAYER);
        }
    }
}
