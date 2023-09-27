package oop.ex6.validator;

/**
 * ParsingException class extends Exception.
 */
public class ParsingException extends Exception {
    private static final String DEFAULT_MESSAGE = "Error while parsing the given file.";
    /**
     * Constructor of ParsingException
     *
     * @param message String describes the error that occur
     */
    public ParsingException(String message) {
        super(message);
    }

    /**
     * default constructor of ParsingException
     */
    public ParsingException() {
        super(DEFAULT_MESSAGE);
    }
}
