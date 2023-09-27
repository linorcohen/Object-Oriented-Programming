package oop.ex6.validator.symbol_table;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

/**
 * Manages a collection of symbol tables, for each scope of the code.
 */
public class SymbolTableManager {
    /**
     * Constants representing the Error Messages
     */
    private static final String UNDECLARED_ASSIGNMENT_ERROR = "Assignment of undeclared variable.";
    public static final String VARIABLE_ALREADY_EXISTS_ERROR = "Variable already exists.";

    /**
     * A symbol table for the global scope of the program.
     */
    private final SymbolTable globalTable;

    /**
     * Linked list of symbol tables for temporary scopes in the program.
     */
    private final LinkedList<SymbolTable> symbolTables;

    /**
     * A set of all uninitialized global variables that were assigned a value
     * in current scope.
     */
    private final HashSet<Variable> globalVariablesAssignments;

    /**
     * SymbolTableManager constructor.
     */
    public SymbolTableManager() {
        this.symbolTables = new LinkedList<>();
        globalTable = new SymbolTable();
        globalVariablesAssignments = new HashSet<>();
    }

    /**
     * creates a new symbol table and puts as the first in the tables list
     */
    public void createNewTable() {
        symbolTables.addFirst(new SymbolTable());
    }

    /**
     * removes the first symbol table from the temporary tables list
     * (the global table cannot be removed).
     * If temporary scope table is empty reverts global variables assignments
     * back to previous state.
     */
    public void removeTable() {
        if (symbolTables.size() != 0) {
            symbolTables.removeFirst();
        }

        if (symbolTables.isEmpty()) {
            revertGlobalVariablesAssignments();
        }
    }

    /**
     * Sets variables in the globalVariablesAssignments set to uninitialized
     * and clears the set.
     */
    private void revertGlobalVariablesAssignments() {
        for (Variable globalVariable : globalVariablesAssignments) {
            globalVariable.setInitializationState(false);
        }
        globalVariablesAssignments.clear();
    }

    /**
     * adds a new variable to the current table.
     *
     * @param varName       String of the variable name
     * @param varType       String of the variable type
     * @param isInitialized String of the variable value
     * @param isFinal       boolean indicates if the variable is final
     * @throws SymbolTableException in case the variable already exists in the table
     */
    public void addVariable(String varName, String varType,
                            boolean isInitialized, boolean isFinal) throws SymbolTableException {
        SymbolTable currentTable = getCurrentTable();

        if (currentTable.tableContains(varName)) {
            throw new SymbolTableException(VARIABLE_ALREADY_EXISTS_ERROR);
        }
        currentTable.addVarToTable(varName, varType, isInitialized, isFinal);
    }

    /**
     * Manages the assignment of a variable.
     * - If the variable is in the temporary scope, it assigns a value to the variable in the table.
     * - If the variable is not found in the temporary scope but is found in the global scope,
     * the global variable will be set to be initialized and this change will be reverted once
     * the temporary scope in which it was initialized is no longer in use.
     *
     * @param varName      The variable to assign.
     * @param requiredType The type of the object the variable should be assigned to.
     * @param isTypeOf     boolean method that returns true iff the first argument can be cast to
     *                     the second argument.
     * @throws SymbolTableException if the variable is not found in the table, variable is found but is
     *                              final, variable type and required type do not match.
     */
    public void assignVariable(String varName, String requiredType,
                               BiFunction<String, String, Boolean> isTypeOf)
            throws SymbolTableException {
        Variable variable = getVariableInTempScope(varName);
        if (variable != null) {
            variable.assign(requiredType, isTypeOf);
            return;
        }

        variable = getVariableInGlobalScope(varName);
        if (variable == null) {
            throw new SymbolTableException(UNDECLARED_ASSIGNMENT_ERROR);
        }

        if (!variable.isInitialized()) {
            globalVariablesAssignments.add(variable);
        }

        variable.assign(requiredType, isTypeOf);
    }

    /**
     * Get the variable with the given name in the innermost scope.
     *
     * @param varName variable name
     * @return Variable object found in the table, or null if variable
     * was not found.
     */
    public Variable getVariable(String varName) {
        Variable variable;
        variable = getVariableInTempScope(varName);
        if (variable == null) {
            variable = getVariableInGlobalScope(varName);
        }
        return variable;
    }

    /**
     * searches for the variable in the temporary scopes,
     * returns null if not found.
     */
    private Variable getVariableInTempScope(String varName) {
        Variable variable = null;
        for (SymbolTable table : symbolTables) {
            try {
                variable = table.get(varName);
            } catch (NoSuchElementException ignored) {
            }
        }
        return variable;
    }


    /**
     * searches for the variable in the global scope,
     * returns null if not found.
     */
    private Variable getVariableInGlobalScope(String varName) {
        Variable variable = null;
        try {
            variable = globalTable.get(varName);
        } catch (NoSuchElementException ignored) {
        }
        return variable;
    }

    /**
     * returns current table (i.e., inner-most scope currently in use)
     */
    private SymbolTable getCurrentTable() {
        if (symbolTables.isEmpty()) {
            return globalTable;
        } else {
            return symbolTables.getFirst();
        }
    }
}
