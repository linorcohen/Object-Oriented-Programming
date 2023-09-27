import java.util.Scanner;

/**
 * HumanPlayer Class.
 * This class represents a human player. Its asks the player for commands using the System.in.
 */
public class HumanPlayer implements Player {

    /**
     * Scanner object to read from System.in
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * private constant representing the error massage for illegal coordinates.
     */
    private static final String ERROR_MESSAGE = "Invalid coordinates, type again: ";

    /**
     * private constant representing the massage for the [layer to insert coordinates.
     */
    private static final String ASK_PLAYER_FOR_COORDINATES_MASSAGE = "Player %s, type coordinates: ";

    /**
     * Default constructor
     */
    HumanPlayer() {
    }

    /**
     * this method plays the human turn by asking the the coordinates from System.in
     *
     * @param board - Board of the game
     * @param mark  - Mark of the player
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        boolean inputInvalid = true;
        System.out.printf((ASK_PLAYER_FOR_COORDINATES_MASSAGE) + "%n", mark);
        while (inputInvalid) {
            String input = scanner.nextLine();
            int row = Integer.parseInt(input.substring(0, 1));
            int col = Integer.parseInt(input.substring(1, 2));
            if ((0 <= row && row <= board.size - 1) && (0 <= col && col <= board.size - 1) &&
                    board.putMark(mark, row, col)) {
                inputInvalid = false;
            } else {
                System.out.println(ERROR_MESSAGE);
            }
        }
    }
}
