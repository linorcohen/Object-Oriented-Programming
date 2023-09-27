/**
 * Game class.
 * An instance of the class represents a single game. the class must know when the game ended,
 * who was the winner and whether it ended in a draw.
 */
public class Game {

    /**
     * private constants for the default win streak
     */
    private static final int DEFAULT_WIN_STREAK = 3;

    /**
     * previous board before current move
     */
    private Mark[][] prevBoard;

    Renderer renderer;
    Player playerX;
    Player playerO;
    Board board;
    int winStreak = DEFAULT_WIN_STREAK;

    /**
     * default constructor , defines a new game, with defaults values.
     *
     * @param playerX  - Player with mark X
     * @param playerO  - Player with mark O
     * @param renderer - Renderer to set the game.
     */
    Game(Player playerX, Player playerO, Renderer renderer) {
        this.renderer = renderer;
        this.playerX = playerX;
        this.playerO = playerO;
        this.board = new Board();
    }

    /**
     * Constructor, set new board size, and winning streak, If an invalid value is entered,
     * the winning streak will be set to equal the size of the board.
     *
     * @param playerX   - Player with mark X
     * @param playerO   - Player with mark O
     * @param size      - int size of the board
     * @param winStreak - int winning streak
     * @param renderer  - Renderer to set the game.
     */
    Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        this.renderer = renderer;
        this.playerX = playerX;
        this.playerO = playerO;
        this.board = new Board(size);
        this.winStreak = putWinStreak(winStreak, size);
    }

    /**
     * private method that checks whether the given winning streak is valid, and returns it.
     * if the winning streak is larger then the size of the board, it returns the size of the board.
     *
     * @param winStreak - int winning streak value
     * @param size      - int size of the board
     * @return int winning streak in case its smaller than the board,otherwise returns size .
     */
    private int putWinStreak(int winStreak, int size) {
        if (winStreak < 0 || winStreak > size) {
            return size;
        }
        return winStreak;
    }

    /**
     * this method returns the instance winning streak value.
     *
     * @return int winning streak
     */
    public int getWinStreak() {
        return this.winStreak;
    }

    /**
     * this method runs the game - from start to finish and when its over returns the winner.
     * The game ends when one of the players has a winning streak or when there are no blanks slots left
     * in the board. In case the game is over in a tie, Mark.BLANK will be returned.
     *
     * @return Mark of the winning player, Mark.BLANK for a tie.
     */
    public Mark run() {
        int boardSize = this.board.getSize();
        Player[] players = {this.playerX, this.playerO};
        Mark[] marks = {Mark.X, Mark.O};

        int i = 0;
        while (!isBoardFull(boardSize)) {
            this.prevBoard = this.board.getBoard();
            this.renderer.renderBoard(this.board);
            players[i % 2].playTurn(this.board, marks[i % 2]);
            if (isWinner(marks[i % 2], boardSize, this.board.getBoard())) {
                return marks[i % 2];
            }
            i++;
        }
        return Mark.BLANK;
    }

    /**
     * private method for checking if one of the players won the game.
     * this method scan the board for a winning streak of the given mark.
     *
     * @param mark      - Mark to check on the board
     * @param boardSize - int size of the board
     * @param nextBoard - Mark[][] current board status
     * @return true in case the given mark has a winning streak, else false.
     */
    private boolean isWinner(Mark mark, int boardSize, Mark[][] nextBoard) {
        int[] coordinates = findLastPlacementOnBoard(nextBoard, boardSize);
        return checkRowAndColForWin(mark, coordinates, boardSize) ||
                checkRightDiagonalForWin(mark, coordinates, boardSize) ||
                checkLeftDiagonalForWin(mark, coordinates, boardSize);
    }

    /**
     * this private method finds the last placement of the game on the board.
     *
     * @param nextBoard - Mark[][] current board status
     * @param boardSize - Mark[][] previews board status
     * @return int[] the coordinates of the last placement found.
     */
    private int[] findLastPlacementOnBoard(Mark[][] nextBoard, int boardSize) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (this.prevBoard[row][col] != nextBoard[row][col]) {
                    return new int[]{row, col};
                }
            }
        }
        return new int[]{0, 0}; // default coordinates
    }

    /**
     * private method that scan the rows and cols of the given coordinates for a win on the board.
     *
     * @param mark        - Mark of the given coordinates
     * @param coordinates - int[] given coordinates
     * @param boardSize   - int board size
     * @return true in case the given mark has a streak, false otherwise.
     */
    private boolean checkRowAndColForWin(Mark mark, int[] coordinates, int boardSize) {
        int countRow = 0;
        int countCol = 0;
        for (int i = 0; i < boardSize; i++) {
            if (this.board.getMark(coordinates[0], i) == mark) {
                countRow++;
            } else {
                countRow = 0;
            }
            if (this.board.getMark(i, coordinates[1]) == mark) {
                countCol++;
            } else {
                countCol = 0;
            }
            if (countCol >= this.winStreak || countRow >= this.winStreak) {
                return true;
            }
        }
        return false;
    }

    /**
     * private method that scan the right diagonal of the given coordinates for a win on the board.
     *
     * @param mark        - Mark of the given coordinates
     * @param coordinates - int[] given coordinates
     * @param boardSize   - int board size
     * @return true in case the given mark has a streak, false otherwise.
     */
    private boolean checkRightDiagonalForWin(Mark mark, int[] coordinates, int boardSize) {
        int diagonalNum = coordinates[0] + coordinates[1];
        int midDiagonal = (((boardSize * 2) - 1) / 2) + 1; // (numOfDiagonals/2)+1 = midDiagonal
        if (0 <= diagonalNum && diagonalNum < midDiagonal) {
            return searchDiagonal(diagonalNum, diagonalNum, 0, mark);
        }
        // diagonalNum%(boardSize-1) = diagonalNum for diagonals >= midDiagonal
        // boardSize-1 = row coordinate for the beginning of diagonals >= midDiagonal
        // diagonalNum - boardSize = col coordinate for the beginning of diagonals >= midDiagonal
        return searchDiagonal(diagonalNum % (boardSize - 1), boardSize - 1, diagonalNum - boardSize, mark);
    }

    /**
     * private method that scan the left diagonal of the given coordinates for a win on the board.
     *
     * @param mark        - Mark of the given coordinates
     * @param coordinates - int[] given coordinates
     * @param boardSize   - int board size
     * @return true in case the given mark has a streak, false otherwise.
     */
    private boolean checkLeftDiagonalForWin(Mark mark, int[] coordinates, int boardSize) {
        int count = 0;
        int curRow = coordinates[0] - Math.min(coordinates[0], coordinates[1]);
        int curCol = coordinates[1] - Math.min(coordinates[0], coordinates[1]);
        while (curRow + 1 < boardSize && curCol + 1 < boardSize) {
            if (this.board.getMark(curRow, curCol) == mark) {
                count++;
            } else {
                count = 0;
            }
            if (count >= this.winStreak) {
                return true;
            }
            curRow++;
            curCol++;
        }
        return false;
    }

    /**
     * private method that checks the current diagonal fo a winning streak of the given nark.
     *
     * @param diagonalNum - int diagonal number
     * @param row         - int the row coordinate of the begging of the diagonal (from bottom to up)
     * @param col         - int the row coordinate of the begging of the diagonal (from bottom to up)
     * @param mark        - Mark to check for winning streak
     * @return true in case the given mark has a streak, false otherwise.
     */
    private boolean searchDiagonal(int diagonalNum, int row, int col, Mark mark) {
        int count = 0;
        for (int i = 0; i <= diagonalNum; i++) {
            if (this.board.getMark(row, col) == mark) {
                count++;
            } else {
                count = 0;
            }
            if (count >= this.winStreak) {
                return true;
            }
            row--;
            col++;
        }
        return false;
    }


    /**
     * private method that checks if the current board is full == no more Mark.BLANK slots.
     *
     * @param boardSize - int board size
     * @return true in case the board is full, false otherwise.
     */
    private boolean isBoardFull(int boardSize) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (this.board.getMark(row, col) == Mark.BLANK) {
                    return false;
                }
            }
        }
        return true;
    }


}
