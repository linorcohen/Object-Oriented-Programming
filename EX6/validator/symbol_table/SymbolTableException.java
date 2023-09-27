package oop.ex6.validator.symbol_table;

/**
 * SymbolTableException class extends Exception.
 */
public class SymbolTableException extends Exception {

    /**
     * Constance representing the default error message
     */
    private static final String DEFAULT_MESSAGE = "Illegal variable access or modification.";

    /**
     * default Constractor of SymbolTableException
     */
    public SymbolTableException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constractor of SymbolTableException
     *
     * @param message String describes the error that occur
     */
    public SymbolTableException(String message) {
        super(message);
    }
}
