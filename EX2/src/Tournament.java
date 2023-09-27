/**
 * Tournament Class.
 * This class represent a game tournament.
 */
public class Tournament {

    /**
     * private constant of the players results massage.
     */
    private static final String RESULTS_MESSAGE = "######### Results #########\n" +
            "Player 1, %s won: %d rounds\n" +
            "Player 2, %s won: %d rounds\n" +
            "Ties: %d";

    /**
     * private constant for error massage of invalid given arguments.
     */
    private static final String ERROR_INVALID_ARGS_MESSAGE = "Choose a player, and start again\n" +
            "The players: [human, clever, whatever, genius]";

    /**
     * private constant for the value of empty score.
     */
    private static final int EMPTY_SCORE = 0;

    int rounds;
    Renderer renderer;
    Player[] players;

    private int winsPlayer1;
    private int winsPlayer2;
    private int ties;

    /**
     * Constructor, set the number of rounds, renderer of the tournament, and list of players.
     *
     * @param rounds   - int number of rounds
     * @param renderer - Renderer for the game
     * @param players  - Players[] list of players of the tournament
     */
    Tournament(int rounds, Renderer renderer, Player[] players) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.players = new Player[players.length];
        System.arraycopy(players, 0, this.players, 0, players.length);
        this.winsPlayer1 = EMPTY_SCORE;
        this.winsPlayer2 = EMPTY_SCORE;
        this.ties = EMPTY_SCORE;
    }

    /**
     * this method runs the tournaments according to the given round number,
     * and prints the results of each game to the console.
     *
     * @param size         - int size of board game
     * @param winStreak    - int winning streak
     * @param playersNames - String[] players names.
     */
    public void playTournament(int size, int winStreak, String[] playersNames) {
        for (int roundNum = 0; roundNum < this.rounds; roundNum++) {
            Game game = new Game(this.players[roundNum % 2], this.players[(roundNum + 1) % 2], size,
                    winStreak, this.renderer);
            Mark winnerMark = game.run();
            setWinnerScore(winnerMark, roundNum);
            System.out.printf((RESULTS_MESSAGE) + "%n", playersNames[0], this.winsPlayer1,
                    playersNames[1], this.winsPlayer2, this.ties);
        }
    }

    /**
     * private method that set the winning result according to the given winning mark.
     * if the round is an even number , Mark.O => Player2 get a point, Mark.X => Player1 get a point
     * else,  Mark.O => Player1 get a point, Mark.X => Player2 get a point
     * if Mark.BLANK => tie get a point.
     *
     * @param winnerMark - Mark of the winner ine game
     * @param roundNum   - int the round number
     */
    private void setWinnerScore(Mark winnerMark, int roundNum) {
        if (winnerMark == Mark.BLANK) {
            this.ties++;
        } else if (roundNum % 2 == 0) {
            if (winnerMark == Mark.X) {
                this.winsPlayer1++;
            } else if (winnerMark == Mark.O) {
                this.winsPlayer2++;
            }
        } else {
            if (winnerMark == Mark.O) {
                this.winsPlayer1++;
            } else if (winnerMark == Mark.X) {
                this.winsPlayer2++;
            }
        }
    }

    /**
     * private method that checks if the list of the given players names are valid.
     *
     * @param playersNames - String[] names of the players
     * @return true if they are valid, false otherwise.
     */
    private static boolean checkPlayerNameIsValid(String[] playersNames) {
        String[] validPlayerNames = {"human", "clever", "whatever", "genius"};
        int numOfValidity = 0;
        for (String playerName : playersNames) {
            for (String validPlayerName : validPlayerNames) {
                if (playerName.equals(validPlayerName)) {
                    numOfValidity++;
                    break;
                }
            }
        }
        return numOfValidity == playersNames.length;
    }

    public static void main(String[] args) {
        int rounds = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        int winStreak = Integer.parseInt(args[2]);
        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer = rendererFactory.buildRenderer(args[3], size);
        String[] playersNames = {args[4].toLowerCase(), args[5].toLowerCase()};

        if (checkPlayerNameIsValid(playersNames)) {
            PlayerFactory playerFactory = new PlayerFactory();
            Player[] players = {playerFactory.buildPlayer(playersNames[0]),
                    playerFactory.buildPlayer(playersNames[1])};
            Tournament tournament = new Tournament(rounds, renderer, players);
            tournament.playTournament(size, winStreak, playersNames);
        } else {
            System.out.println(ERROR_INVALID_ARGS_MESSAGE);
        }
    }
}
