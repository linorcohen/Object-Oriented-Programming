/**
 * Board class:
 * This class is responsible for the condition of the board:
 * the size of the board, marking the board and keeping everything marked on the board.
 * The numbering of the slots on the board starts with 0.
 */
public class Board {
    /**
     * Constant of the default size of the board
     */
    private static final int DEFAULT_SIZE = 4;

    Mark[][] board;
    int size;

    /**
     * Default Constructor. set new empty board with its default size.
     */
    Board() {
        this.board = new Mark[DEFAULT_SIZE][DEFAULT_SIZE];
        this.size = DEFAULT_SIZE;
        initializeBoard();
    }

    /**
     * Constructor. set new empty board with its given size.
     */
    Board(int size) {
        this.board = new Mark[size][size];
        this.size = size;
        initializeBoard();
    }

    /**
     * This function initialize the board with Mark.BLANK.
     */
    private void initializeBoard() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.board[i][j] = Mark.BLANK;
            }
        }
    }

    /**
     * This function returns the current board size
     *
     * @return int size of board
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Returns the 2D array that represents the board
     *
     * @return Mark[][] board
     */
    public Mark[][] getBoard() {
        Mark[][] newBoard = new Mark[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            System.arraycopy(this.board[i], 0, newBoard[i], 0, this.size);
        }
        return newBoard;
    }

    /**
     * this function will check if the given mark can be replaced in the given coordinates,
     * and replace it if so.
     * returns true in case the given mark can be places, false otherwise.
     *
     * @param mark - Mark to put
     * @param row  - int row coordinate
     * @param col  - int col coordinate
     * @return true in case the given mark can be put in the given coordinates, false otherwise.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (row < 0 || row >= this.size || col < 0 || col >= this.size || board[row][col] != Mark.BLANK) {
            return false;
        }
        this.board[row][col] = mark;
        return true;
    }

    /**
     * Return the mark that is in the given slot. In case of
     * Invalid coordinates will return Mark.BLANK.
     *
     * @param row - int row coordinate
     * @param col - int col coordinate
     * @return Mark in the given coordinates
     */
    public Mark getMark(int row, int col) {
        if (row < 0 || row >= this.size || col < 0 || col >= this.size) {
            return Mark.BLANK;
        }
        return this.board[row][col];
    }

}
