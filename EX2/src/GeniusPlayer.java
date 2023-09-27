/**
 * GeniusPlayer Class.
 * This class represents a genius player.
 * this player has strategy that wins the clever player most of the times.
 */
public class GeniusPlayer implements Player {

    private int prevRow;
    private int prevCol;
    private boolean isFirstTurn = true;

    /**
     * Default constructor
     */
    GeniusPlayer() {
    }

    /**
     * this method plays the turn of the genius according to the given strategy:
     * if its the player first turn - place mark on the next empty slot on board.
     * else:
     * if last placement right box is empty - place mark.
     * if last placement left box is empty - place mark.
     * last placement down box is empty - place mark.
     * else:
     * place on the next empty slot
     *
     * @param board - Board of the game
     * @param mark  - Mark of the player
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        if (this.isFirstTurn) {
            findFirstEmptyPlacement(board, mark);
            this.isFirstTurn = false;
        } else if (this.prevCol + 1 < board.getSize() &&
                board.getMark(this.prevRow, this.prevCol + 1) == Mark.BLANK) { // right box
            board.putMark(mark, this.prevRow, this.prevCol + 1);
            this.prevCol++;
        } else if (this.prevCol - 1 >= 0 &&
                board.getMark(this.prevRow, this.prevCol - 1) == Mark.BLANK) { //left box
            board.putMark(mark, this.prevRow, this.prevCol - 1);
            this.prevCol--;
        } else if (this.prevRow - 1 >= 0 &&
                board.getMark(this.prevRow - 1, this.prevCol) == Mark.BLANK) { // down box
            board.putMark(mark, this.prevRow - 1, this.prevCol);
            this.prevRow--;
        } else {
            findFirstEmptyPlacement(board, mark); // in case no strategy place is empty
        }
    }

    /**
     * private method that find the first nxt empty slot on the board, and place the given mark there.
     *
     * @param board - Board of the game
     * @param mark  - Mark of the player
     */
    private void findFirstEmptyPlacement(Board board, Mark mark) {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getMark(i, j) == Mark.BLANK) {
                    board.putMark(mark, i, j);
                    return;
                }
            }
        }
    }
}

