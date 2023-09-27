package oop.ex6.validator.symbol_table;

import java.util.*;

/**
 * A single symbol table, responsible for storing information about variables.
 */
public class SymbolTable {

    /**
     * Map holding all information about variables in a scope
     */
    private final Map<String, Variable> table;

    /**
     * SymbolTable Constructor.
     */
    public SymbolTable() {
        table = new HashMap<>();
    }

    /**
     * method checks if the symbol table continues the given variable
     *
     * @param varName String of the variable name
     * @return true if the variable in the table, else false.
     */
    public boolean tableContains(String varName) {
        return table.containsKey(varName);
    }

    /**
     * adds a new variable to the symbol table.
     * this method assume that the given arguments are valid.
     *
     * @param varName String of the variable name
     * @param varType String of the variable type
     * @param isInitialized   boolean indicates if the variable is initialized
     * @param isFinal boolean indicates if the variable is final
     */
    public Variable addVarToTable(String varName, String varType, boolean isInitialized, boolean isFinal)
    throws SymbolTableException {
        Variable variable = new Variable(varName, varType, isInitialized, isFinal);
        table.put(varName, variable);
        return variable;
    }

    /**
     * extracts the Variable object by name from the table.
     * @param varName variable name
     * @return Variable object matching the variable's name or null if not found
     */
    public Variable get(String varName) {
        return table.get(varName);
    }
}