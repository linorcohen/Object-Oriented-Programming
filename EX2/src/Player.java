/**
 * Interface Player.
 */
public interface Player {

    /**
     * play the current player turn in the game.
     * @param board - Board of the game
     * @param mark - Mark of the player
     */
    void playTurn(Board board, Mark mark);

}
