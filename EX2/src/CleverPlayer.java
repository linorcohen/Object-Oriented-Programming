import java.util.Random;

/**
 * CleverPlayer Class.
 * This class represents a clever player.
 * this player has strategy that wins the whatever player most of the times.
 */
public class CleverPlayer implements Player {

    /**
     * private Random object
     */
    private final Random rand = new Random();

    /**
     * private constance of the size of box to check size
     */
    private final int CHECK_BOX_SIZE = 3;

    private int prevRow;
    private int prevCol;
    private boolean isFirstTurn = true;

    /**
     * Default constructor
     */
    CleverPlayer() {
    }

    /**
     * this method plays the turn of the clever according to the given strategy:
     * if its the player first turn - place mark randomly on board.
     * else:
     * place the mark on the first available slot int around the last placement.
     * else:
     * place on the next available random slot.
     *
     * @param board - Board of the game
     * @param mark  - Mark of the player
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        if (this.isFirstTurn) {
            randomPlacement(board, mark);
            isFirstTurn = false;
        } else {
            int curRow = this.prevRow - 1;
            int curCol = this.prevCol - 1;
            for (int i = 0; i < CHECK_BOX_SIZE; i++) {
                for (int j = 0; j < CHECK_BOX_SIZE; j++) {
                    curCol += j;
                    if (curRow < board.getSize() && curRow >= 0 && curCol < board.getSize() && curCol >= 0) {
                        if (board.putMark(mark, curRow, curCol)) {
                            this.prevRow = curRow;
                            this.prevCol = curCol;
                            return;
                        }
                    }
                }
                curRow += i;
            }
        }
        randomPlacement(board, mark);
    }

    /**
     * private method for placing the given mark on the next available slot randomly.
     *
     * @param board - Board of the game
     * @param mark  - Mark of the player
     */
    private void randomPlacement(Board board, Mark mark) {
        boolean invalidPlacement = true;
        while (invalidPlacement) {
            this.prevRow = rand.nextInt(board.getSize());
            this.prevCol = rand.nextInt(board.getSize());
            if (board.putMark(mark, this.prevRow, this.prevCol)) {
                invalidPlacement = false;
            }
        }
    }


}
