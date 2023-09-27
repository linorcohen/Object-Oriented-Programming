package oop.ex6.validator.methods_container;

/**
 * MethodsContainerException class extends Exception.
 */
public class MethodsContainerException extends RuntimeException {

    /**
     * Constance representing the default error message
     */
    private static final String DEFAULT_MESSAGE = "Invalid method call or declaration.";

    /**
     * default Constractor of MethodsContainerException
     */
    public MethodsContainerException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constractor of MethodsContainerException
     *
     * @param message String describes the error that occur
     */
    public MethodsContainerException(String message) {
        super(message);
    }
}
