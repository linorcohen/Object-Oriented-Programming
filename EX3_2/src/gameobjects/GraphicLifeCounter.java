package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * class GraphicLifeCounter.
 * This class inherits from GameObject, and describes the heart
 * (the amount of graphical disqualification) in the game.
 */
public class GraphicLifeCounter extends GameObject {

    /**
     * constance representing the max life value in the game
     */
    private static final int MAX_LIFE_VALUE = 4;
    /**
     * constance representing the space between hearts
     */
    private static final int SPACE_BETWEEN_HEARTS = 2;
    /**
     * GameObject array contain all hearts objects in the game.
     */
    private final Heart[] hearts;

    private final Counter livesCounter;
    private final GameObjectCollection gameObjectsCollection;
    private final Vector2 widgetTopLeftCorner;
    private final Vector2 widgetDimensions;
    private final Renderable widgetRenderable;
    private int numOfLives;

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

        super(Vector2.ZERO, Vector2.ZERO, null);
        this.livesCounter = livesCounter;
        this.widgetTopLeftCorner = widgetTopLeftCorner;
        this.widgetDimensions = widgetDimensions;
        this.widgetRenderable = widgetRenderable;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
        this.hearts = new Heart[MAX_LIFE_VALUE];

        addHeartsToGameObjects();
    }

    /**
     * Private method.
     * This method creates numOfLives hearts, and adds them to the game.
     */
    private void addHeartsToGameObjects() {
        for (int i = 0; i < numOfLives; i++) {
            hearts[i] = getHeartObject(i);
            gameObjectsCollection.addGameObject(hearts[i], Layer.UI);
            hearts[i].setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        }
    }

    /**
     * private method.
     * this method creates a heart object according to the top left parameter given
     *
     * @param topLeftPlacement top left placement for the current heart
     * @return heart object
     */
    private Heart getHeartObject(int topLeftPlacement) {
        return new Heart(widgetTopLeftCorner.add(new Vector2(
                topLeftPlacement * (widgetDimensions.x() + SPACE_BETWEEN_HEARTS), 0)),
                widgetDimensions, widgetRenderable, livesCounter);
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
            gameObjectsCollection.removeGameObject(hearts[numOfLives], Layer.UI);
            hearts[numOfLives] = null;
        }
        if (livesCounter.value() > numOfLives && livesCounter.value() <= MAX_LIFE_VALUE) {
            hearts[numOfLives] = getHeartObject(numOfLives);
            gameObjectsCollection.addGameObject(hearts[numOfLives], Layer.UI);
            hearts[numOfLives].setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

            if (numOfLives < MAX_LIFE_VALUE) {
                numOfLives++;
            }
        }
    }

}
