package oop.ex6.validator;

/**
 * InvalidSyntaxException class extends Exception.
 */
public class InvalidSyntaxException extends Exception {

    /**
     * Constance representing the default error message
     */
    private static final String DEFAULT_MESSAGE = "Invalid S-java syntax.";

    /**
     * Constructor of InvalidSyntaxException
     *
     * @param message String describes the error that occur
     */
    public InvalidSyntaxException(String message) {
        super(message);
    }

    /**
     * default Constructor of InvalidSyntaxException
     */
    public InvalidSyntaxException() {
        super(DEFAULT_MESSAGE);
    }
}
