package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Shell class.
 * this class represent the user interface operations.
 */
public class Shell {

    /**
     * private constance represent the error massage for invalid format
     */
    private static final String ERROR_MASSAGE_INVALID_FORMAT = "Did not %s due to incorrect format\n";
    /**
     * private constance represent the error massage for invalid command
     */
    private static final String ERROR_MASSAGE_INVALID_COMMAND = "Did not executed due to incorrect command";
    /**
     * private constance represent the error massage for invalid bounds
     */
    private static final String ERROR_MASSAGE_INVALID_BOUNDS = "Did not change due to exceeding " +
            "boundaries";
    /**
     * private constance represent the massage of changed bounds
     */
    private static final String BOUNDS_CHANGED_MASSAGE = "Width set to %s\n";
    /**
     * private constance represent the initial number of char in a row
     */
    private static final int INITIAL_CHARS_IN_ROW = 64;
    /**
     * private constance represent the minimum pixel for a char
     */
    private static final int MIN_PIXELS_PER_CHAR = 2;
    /**
     * private constance represent the first char ascii value
     */
    private static final int FIRST_CHAR_ASCII = 32;
    /**
     * private constance represent the last char ascii value
     */
    private static final int LAST_CHAR_ASCII = 126;
    /**
     * private constance represent the length of two params command
     */
    private static final int TWO_PARAMS_COMMAND_LENGTH = 2;
    /**
     * private constance represent the ascii value of the char 0
     */
    private static final int CHAR_ZERO_ASCII = 48;
    /**
     * private constance represent the ascii value of the char 9
     */
    private static final int CHAR_NINE_ASCII = 57;
    /**
     * private constance represent the number 2
     */
    private static final int TWO = 2;
    /**
     * private constance represent the number 1
     */
    private static final int ONE = 1;
    /**
     * private constance represent the number 0
     */
    private static final int ZERO = 0;
    /**
     * private constance represent the exit command
     */
    private static final String EXIT = "exit";
    /**
     * private constance represent the chars command
     */
    private static final String CHARS = "chars";
    /**
     * private constance represent the render command
     */
    private static final String RENDER = "render";
    /**
     * private constance represent the console command
     */
    private static final String CONSOLE = "console";
    /**
     * private constance represent the add command
     */
    private static final String ADD = "add";
    /**
     * private constance represent the remove command
     */
    private static final String REMOVE = "remove";
    /**
     * private constance represent the resize command
     */
    private static final String RESIZE = "res";
    /**
     * private constance represent the down command
     */
    private static final String DOWN = "down";
    /**
     * private constance represent the up command
     */
    private static final String UP = "up";
    /**
     * private constance represent the output file name
     */
    private static final String OUTPUT_FILENAME = "out.html";
    /**
     * private constance represent the output font name
     */
    private static final String OUTPUT_FONT_NAME = "Courier New";
    /**
     * private constance represent the input format
     */
    private static final String INPUT_PRINT_FORMAT = ">>> ";
    /**
     * private constance represent the all command
     */
    private static final String ALL = "all";
    /**
     * private constance represent the space command
     */
    private static final String SPACE = "space";
    /**
     * private constance represent the char range command
     */
    private static final String CHAR_RANGE = "charRange";
    /**
     * private constance represent the single char command
     */
    private static final String SINGLE_CHAR = "singleChar";
    /**
     * private constance represent the hyphen
     */
    private static final String HYPHEN = "-";
    /**
     * private constance represent the space char
     */
    private static final String SPACE_STR = " ";

    /**
     * private field of scanner object
     */
    private final Scanner scanner;
    /**
     * private field of minimum char in row
     */
    private final int minCharsInRow;
    /**
     * private field of maximum char im row
     */
    private final int maxCharsInRow;
    /**
     * private field of BrightnessImgCharMatcher object
     */
    private final BrightnessImgCharMatcher charMatcher;
    /**
     * private field of AsciiOutput object
     */
    private AsciiOutput asciiOutput;
    /**
     * private field of the number char in row
     */
    private int charsInRow;
    /**
     * private field of the char set
     */
    private Set<Character> charsSet;

    /**
     * Shell constructor.
     * this method creates a shell object.
     *
     * @param img image to render.
     */
    public Shell(Image img) {
        this.scanner = new Scanner(System.in);
        this.charMatcher = new BrightnessImgCharMatcher(img, OUTPUT_FONT_NAME);
        this.minCharsInRow = Math.max(ONE, img.getWidth() / img.getHeight());
        this.maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        this.charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        this.asciiOutput = new HtmlAsciiOutput(OUTPUT_FILENAME, OUTPUT_FONT_NAME);
        initializeCharSet();
    }

    /**
     * this method role is to get commands from the user and translate them to command the class will operate.
     */
    public void run() {
        String userInput = getInputFromUser();
        while (!userInput.equals(EXIT)) {
            String[] userInputList = userInput.split(SPACE_STR);
            if (userInputList.length != ZERO) {
                switch (userInputList[ZERO]) {
                    case CHARS:
                        printAllCharsInSet();
                        break;
                    case RENDER:
                        renderCommand();
                        break;
                    case CONSOLE:
                        consoleCommand();
                        break;
                    case ADD:
                    case REMOVE:
                        boolean isAddCommand = userInputList[ZERO].equals(ADD);
                        addOrRemoveCommand(isAddCommand, userInputList);
                        break;
                    case RESIZE:
                        resCommand(userInputList);
                        break;
                    default:
                        System.out.println(ERROR_MASSAGE_INVALID_COMMAND);
                }
            } else {
                System.out.println(ERROR_MASSAGE_INVALID_COMMAND);
            }
            userInput = getInputFromUser();
        }
    }

    /**
     * private method - this method initialized the class char set field with the chars 0-9
     */
    private void initializeCharSet() {
        this.charsSet = new HashSet<>();
        for (int i = CHAR_ZERO_ASCII; i <= CHAR_NINE_ASCII; i++) {
            charsSet.add((char) i);
        }
    }

    /**
     * private method - this method gets the user input.
     *
     * @return string of the user input
     */
    private String getInputFromUser() {
        System.out.print(INPUT_PRINT_FORMAT);
        return scanner.nextLine().strip(); // <- "   exit   "
    }

    /**
     * private method - this method responsible for the add or remove commands. given the user input it
     * operate the correct command according to the input type
     *
     * @param isAddCommand  true if its an add command, false for remove command
     * @param userInputList list of the user commands
     */
    private void addOrRemoveCommand(boolean isAddCommand, String[] userInputList) {
        try {
            String commandType = getCommandType(userInputList);
            switch (commandType) {
                case ALL:
                    addOrRemoveCharRangeToCharSet(isAddCommand, FIRST_CHAR_ASCII, LAST_CHAR_ASCII);
                    break;
                case SPACE:
                    addOrRemoveCharToCharSet(isAddCommand, SPACE_STR.charAt(ZERO));
                    break;
                case CHAR_RANGE:
                    char[] params = getRangeParameters(userInputList[ONE]);
                    addOrRemoveCharRangeToCharSet(isAddCommand, params[ZERO], params[ONE]);
                    break;
                case SINGLE_CHAR:
                    char c = getSingleCharParameter(userInputList[ONE]);
                    addOrRemoveCharToCharSet(isAddCommand, c);
                    break;
            }
        } catch (IllegalArgumentException e) {
            if (isAddCommand) {
                System.out.printf(ERROR_MASSAGE_INVALID_FORMAT, ADD);
            } else {
                System.out.printf(ERROR_MASSAGE_INVALID_FORMAT, REMOVE);
            }
        }
    }

    /**
     * private method - this method gets the user input list and returns its command type.
     *
     * @param userInputList list of the user commands
     * @return command type
     * @throws IllegalArgumentException in case the user input is illegal, or incorrect
     */
    private String getCommandType(String[] userInputList) throws IllegalArgumentException {
        if (userInputList.length == TWO_PARAMS_COMMAND_LENGTH) {
            if (userInputList[ONE].length() == ONE) {
                return SINGLE_CHAR;
            }
            String[] charRange = userInputList[ONE].split(HYPHEN);
            if (charRange.length == TWO_PARAMS_COMMAND_LENGTH) {
                return CHAR_RANGE;
            }
            if (userInputList[ONE].equals(ALL) || userInputList[ONE].equals(SPACE)) {
                return userInputList[ONE];
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * private method - this method returns the range parameters (in array of chars)
     * to use for command charRange
     *
     * @param userParameter list of the user commands
     * @return char of range parameters
     * @throws IllegalArgumentException in case the user input is illegal, or incorrect
     */
    private char[] getRangeParameters(String userParameter) throws IllegalArgumentException {
        String[] userParameterList = userParameter.split(HYPHEN);
        char[] charParams = new char[]{userParameterList[ZERO].charAt(ZERO),
                userParameterList[ONE].charAt(ZERO)};
        boolean invalidCharParams = userParameterList[ZERO].length() != ONE ||
                userParameterList[ONE].length() != ONE ||
                (charParams[ZERO] > LAST_CHAR_ASCII || charParams[ZERO] < FIRST_CHAR_ASCII) ||
                (charParams[ONE] > LAST_CHAR_ASCII || charParams[ONE] < FIRST_CHAR_ASCII);

        if (invalidCharParams) {
            throw new IllegalArgumentException();
        }
        if (charParams[ZERO] > charParams[ONE]) {
            return new char[]{charParams[ONE], charParams[ZERO]};
        }
        return charParams;
    }

    /**
     * private method - this method gets a string of char params, convert it to char and returns
     * it in case its a legal char.
     *
     * @param charString a given char in string
     * @return the given char as type char
     * @throws IllegalArgumentException in case the user input is illegal, or incorrect
     */
    private char getSingleCharParameter(String charString) throws IllegalArgumentException {
        if (charString.length() != ZERO) {
            char c = charString.charAt(ZERO);
            if (c <= LAST_CHAR_ASCII && c >= FIRST_CHAR_ASCII) {
                return c;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * private method -  this method add of remove, according to the given isAddCommand parameter,
     * a range of chars from the chat set
     *
     * @param isAddCommand true if its an add command, false for remove command
     * @param start        index start of range
     * @param end          index end of range
     */
    private void addOrRemoveCharRangeToCharSet(boolean isAddCommand, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (isAddCommand) {
                charsSet.add((char) i);
            } else {
                charsSet.remove((char) i);
            }
        }
    }

    /**
     * private method - this method add of remove, according to the given isAddCommand parameter,
     * a single char from the char set.
     *
     * @param isAddCommand true if its an add command, false for remove command
     * @param c            the given char to add or remove
     */
    private void addOrRemoveCharToCharSet(boolean isAddCommand, char c) {
        if (isAddCommand) {
            charsSet.add(c);
        } else {
            charsSet.remove(c);
        }
    }

    /**
     * private method - this method oparates the resize of the the number  chars in row according to the
     * user input. prints error message in case resize is not legal.
     *
     * @param userInputList list of the user commands
     */
    private void resCommand(String[] userInputList) {
        if (!(userInputList.length != TWO_PARAMS_COMMAND_LENGTH || (!userInputList[ONE].equals(UP) &&
                !userInputList[ONE].equals(DOWN)))) {
            boolean isUpCommand = userInputList[ONE].equals(UP);
            if (isUpCommand && charsInRow * TWO <= maxCharsInRow) { // up command
                charsInRow *= TWO;
                System.out.printf(BOUNDS_CHANGED_MASSAGE, charsInRow);
            } else if (!isUpCommand && charsInRow / TWO >= minCharsInRow) { // down command
                charsInRow /= TWO;
                System.out.printf(BOUNDS_CHANGED_MASSAGE, charsInRow);
            } else {
                System.out.println(ERROR_MASSAGE_INVALID_BOUNDS); // out of bounds
            }
        } else {
            System.out.println(ERROR_MASSAGE_INVALID_COMMAND);
        }
    }

    /**
     * private method - this method operates the render command and convert the image to Ascii art
     * according to the current char set, and num of char in row.
     * this method also gets its output.
     */
    private void renderCommand() {
        Character[] charSet = convertSetToArray();
        char[][] charImg = charMatcher.chooseChars(charsInRow, charSet);
        asciiOutput.output(charImg);
    }

    /**
     * private method - this method convert the current char set, to a char array.
     *
     * @return char array
     */
    private Character[] convertSetToArray() {
        Character[] charArray = new Character[charsSet.size()];
        int i = ZERO;
        for (char c : charsSet) {
            charArray[i] = c;
            i++;
        }
        return charArray;
    }

    /**
     * private method - this method operates the console command and change the ascii output
     * to console output object.
     */
    private void consoleCommand() {
        asciiOutput = new ConsoleAsciiOutput();
    }

    /**
     * private method - this method prints all the current char set to the user interface.
     */
    private void printAllCharsInSet() {
        for (char c : charsSet) {
            System.out.print(c + SPACE_STR);
        }
        System.out.println();
    }
}
