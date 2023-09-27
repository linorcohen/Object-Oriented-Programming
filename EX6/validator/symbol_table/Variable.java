package oop.ex6.validator.symbol_table;

import java.util.function.BiFunction;

/**
 * Stores information about a single variable.
 */
public class Variable {

    /**
     * Constants representing the Error Messages:
     */
    private static final String FINAL_ASSIGNMENT_ERROR = "Assignment to final variable.";
    private static final String TYPE_MISMATCH_ASSIGNMENT = "Assignment with mismatched types.";
    private static final String FINAL_VARIABLE_NOT_INITIALIZED = "A final variable was not initialized.";

    private final String name;
    private final String type;
    private final boolean isFinal;
    private boolean isInitialized;

    /**
     * Variable constructor.
     * @param name variable's name,
     * @param type variable's type.
     * @param isInitialized variable's initialization status.
     * @param isFinal variable's final modifier.
     * @throws SymbolTableException In case variable cannot be created.
     */
    public Variable(String name, String type, boolean isInitialized, boolean isFinal)
            throws SymbolTableException {
        if (isFinal && !isInitialized) {
            throw new SymbolTableException(FINAL_VARIABLE_NOT_INITIALIZED);
        }
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.isInitialized = isInitialized;
    }

    /**
     * Sets isInitialized to true, or does nothing if value is already initialized.
     * @throws SymbolTableException in case variable is final or types mismatch.
     */
    public void assign(String requiredType, BiFunction<String, String, Boolean> isTypeOf)
            throws SymbolTableException {
        if (isFinal) {
            throw new SymbolTableException(FINAL_ASSIGNMENT_ERROR + name);
        }
        if (!isTypeOf.apply(type, requiredType)) {
            throw new SymbolTableException(TYPE_MISMATCH_ASSIGNMENT);
        }
        isInitialized = true;
    }

    /**
     * return the variable name
     */
    public String getName() {
        return name;
    }

    /**
     * return the variable type
     */
    public String getType() {
        return type;
    }

    /**
     * return if variable is final
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * return if variable is initialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Setter for isInitialized.
     * @param state boolean indicates if the variable is initialized.
     */
    public void setInitializationState(boolean state) {
        isInitialized = state;
    }
}
