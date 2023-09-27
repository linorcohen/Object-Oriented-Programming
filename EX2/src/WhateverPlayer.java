import java.util.Random;

/***
 * Whatever class.
 * this class represent a player that plays his turn by selecting a random placement.
 */
public class WhateverPlayer implements Player {

    /**
     * random object
     */
    private final Random rand = new Random();

    /**
     * default constructor
     */
    WhateverPlayer() {
    }

    /**
     * this method plays the whatever turn by put a mark in a random place.
     *
     * @param board - Board of the game
     * @param mark  - Mark of the player
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        boolean validPlacement;
        do {
            int randomRow = rand.nextInt(board.getSize());
            int randomCol = rand.nextInt(board.getSize());
            validPlacement = board.putMark(mark, randomRow, randomCol);
        } while (!validPlacement);
    }
}
