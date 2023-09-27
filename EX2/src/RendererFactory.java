/**
 * Renderer Factory class.
 * this class is a factory to create a renderer by types.
 */
public class RendererFactory{

    /**
     * this function builds the game renderer according to the given type.
     * "none" - represents VoidRenderer
     * "console" - represents ConsoleRenderer
     * @param type - String represents the type of the renderer
     * @param size - the size of the board (ConsoleRenderer argument)
     * @return Renderer of ConsoleRenderer or VoidRenderer. null otherwise.
     */
    public Renderer buildRenderer(String type, int size) {
        switch (type) {
            case "console":
                return new ConsoleRenderer(size);
            case "none":
                return new VoidRenderer();
            default:
                return null;
        }
    }

}
