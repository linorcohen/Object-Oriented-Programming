package oop.ex6.validator;

import oop.ex6.validator.methods_container.MethodsContainer;
import oop.ex6.validator.methods_container.Parameter;
import oop.ex6.validator.symbol_table.SymbolTableManager;
import oop.ex6.validator.symbol_table.Variable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Responsible for checking types and logic validity.
 */
public class Parser {
    /**
     * Constance representing the Error Messages:
     */
    private static final String MISSING_CLOSING_BRACKET_ERROR = "Missing closing bracket.";
    private static final String METHOD_CALL_IN_GLOBAL_SCOPE_ERROR = "Method call inside global scope.";
    private static final String CALL_TO_NON_EXISTENT_METHOD_ERROR = "Method call to non-existent method: ";
    private static final String NUMBER_OF_ARGUMENTS_TO_METHOD_CALL_ERROR =
            "Invalid number of arguments to Method call.";
    private static final String NON_BOOLEAN_VARIABLE_TYPE_IN_CONDITION_ERROR =
            "Non-boolean variable type in a condition statement.";
    private static final String METHOD_CALL_NOT_MATCH_DECLARATION =
            "Method call arguments do not match method's declaration.";
    private static final String ILLEGAL_CONDITIONAL_STATEMENT = "Illegal conditional statement";
    private static final String USAGE_OF_UNINITIALIZED_VARIABLE = "Usage of uninitialized variable";
    private static final String MISSING_RETURN_STATEMENT = "Missing return statement";
    private static final String NESTED_METHOD_DECLARATION = "Method declaration inside a method body.";
    private static final String INVALID_SYNTAX_ERROR = "Invalid syntax: '";
    private static final String SIGNAL_QUOTATION = "'";
    private static final String COMMA_REGEX = ",";
    private static final String INVALID_EXPRESSION_ERROR = "Invalid expression: ";
    private static final String ATTEMPTED_TO_ASSIGN_ERROR = "Attempted to assign ";
    private static final String TO_STRING = " to ";

    private BufferedReader reader;
    private final SymbolTableManager symbolTableManager;
    private final MethodsContainer methodsContainer;
    private String currentLine;
    private int currentLineIndex;


    /**
     * Parser Constructor.
     *
     * @param reader BufferedReader to read the file with
     */
    public Parser(BufferedReader reader) {
        this.reader = reader;
        this.symbolTableManager = new SymbolTableManager();
        this.methodsContainer = new MethodsContainer();
        this.currentLine = null;
        this.currentLineIndex = 0;
    }

    /**
     * Sets a different reader.
     * Resets currentLine and currentLineIndex.
     *
     * @param reader BufferedReader object
     */
    public void setReader(BufferedReader reader) {
        this.reader = reader;
        currentLine = null;
        currentLineIndex = 0;
    }

    /**
     * Getter for currentLineIndex
     *
     * @return current line's index
     */
    public int getCurrentLineIndex() {
        return currentLineIndex;
    }

    /**
     * Goes over the file and parses variable assignments, declarations and
     * method declarations in the global scope, and fills the global scope's symbol table.
     * Skips empty lines, comment lines and method declaration scopes.
     *
     * @throws Exception If an Exception error occurs
     */
    public void initialParsing() throws Exception {
        while (advanceLine()) {
            if (SyntaxVerifier.isVariableDeclaration(currentLine)) {
                parseVariableDeclaration();
            } else if (SyntaxVerifier.isVariableAssignment(currentLine)) {
                parseVariableAssignment();
            } else if (SyntaxVerifier.isMethodDeclaration(currentLine)) {
                parseMethodDeclaration();
                advanceNextBlock();
            } else if (SyntaxVerifier.isMethodCallStatement(currentLine)) {
                throw new ParsingException(METHOD_CALL_IN_GLOBAL_SCOPE_ERROR);
            } else if (SyntaxVerifier.isIfOrWhileStatement(currentLine)) {
                throw new ParsingException(ILLEGAL_CONDITIONAL_STATEMENT);
            } else {
                throw new InvalidSyntaxException(INVALID_SYNTAX_ERROR + currentLine + SIGNAL_QUOTATION);
            }
        }
    }

    /**
     * perform the main parsing of each method,  and check is content validity.
     *
     * @throws Exception If an Exception error occurs
     */
    public void mainParsing() throws Exception {
        while (findMethodDeclaration()) {
            parseMethodStatement();
        }
    }

    /**
     * this method advance bufferReader line to the next method declaration
     *
     * @return true in case of success, false otherwise
     * @throws IOException If an I/O error occurs
     */
    private boolean findMethodDeclaration() throws IOException {
        while (advanceLine()) {
            if (SyntaxVerifier.isMethodDeclaration(currentLine)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Advances currentLine. Skip comments and empty lines.
     * Returns true if end of file wasn't reached.
     *
     * @throws IOException If an I/O error occurs
     */
    private boolean advanceLine() throws IOException {
        while ((currentLine = reader.readLine()) != null
                && SyntaxVerifier.isRedundant(currentLine)) {
            currentLineIndex++;
        }
        currentLineIndex++;
        return currentLine != null;
    }

    /**
     * Advances currentLine to the next block (until closing bracket line is reached).
     * Should be called only if currentLine is not in global scope.
     * Throws InvalidSyntax error in case currentLine reached the end of file without
     * finding a closing bracket.
     *
     * @throws IOException If an I/O error occurs
     */
    private void advanceNextBlock() throws Exception {
        int numberOfOpenBrackets = 0;
        do {
            if (SyntaxVerifier.OPENING_BRACKET_PATTERN.matcher(currentLine).find()) {
                numberOfOpenBrackets++;
            } else if (SyntaxVerifier.isClosingBracket(currentLine)) {
                numberOfOpenBrackets--;
            }
            if (numberOfOpenBrackets == 0) {
                return;
            }
        }
        while (advanceLine());
        // if reached end of file without returning
        throw new InvalidSyntaxException(MISSING_CLOSING_BRACKET_ERROR);
    }

    /**
     * Parses a syntactically legal variable declaration statements.
     * Adds the variables to scope's symbol table, which may throw Exception
     * in case of illegal values.
     *
     * @throws Exception in case the variable already exists in the table
     */
    private void parseVariableDeclaration() throws Exception {
        Matcher matcher = SyntaxVerifier.MODIFIER_AND_TYPE.matcher(currentLine);
        matcher.find();
        boolean isFinal = (matcher.group(1) != null);
        String varType = matcher.group(2).trim();

        matcher = SyntaxVerifier.VARS_DECLARATION_PATTERN.matcher(currentLine);
        while (matcher.find()) {
            String varName = matcher.group(1).trim();
            String valueExpression = matcher.group(3);

            if (valueExpression == null) {
                symbolTableManager.addVariable(varName, varType, false, isFinal);
                continue;
            }
            valueExpression = valueExpression.trim();
            verifyExpressionTypeAndInitialization(varType, valueExpression);
            symbolTableManager.addVariable(varName, varType, true, isFinal);
        }
    }

    /**
     * Parses a syntactically legal variable assignment statements.
     * Uses symbolTableManager's assign that may throw Exceptions.
     */
    private void parseVariableAssignment() throws Exception {
        Matcher matcher = SyntaxVerifier.VARS_DECLARATION_PATTERN.matcher(currentLine);
        while (matcher.find()) {
            assignVariable(matcher);
        }
    }

    /**
     * this method assign the given variable
     *
     * @param matcher object reset to var declaration pattern
     */
    private void assignVariable(Matcher matcher) throws Exception {
        String varName = matcher.group(1);
        if (varName != null) {
            varName = varName.trim();
        }
        String varValue = matcher.group(3);
        if (varValue != null) {
            varValue = varValue.trim();
        }

        String valueType = getExpressionTypeAndVerifyVariableInitialization(varValue);
        if (valueType == null) {
            throw new ParsingException();
        }

        symbolTableManager.assignVariable(varName, valueType, SyntaxVerifier::isTypeOf);
    }

    /**
     * Parses a syntactically legal method declaration statements.
     * Adds the method arguments to method container.
     *
     * @throws Exception in case the method already exists in the method table,
     *                   or the argument syntax is illegal
     */
    private void parseMethodDeclaration() throws Exception {
        Matcher matcher = SyntaxVerifier.METHOD_DECLARATION_PATTERN.matcher(currentLine);
        matcher.find();
        String methodName = matcher.group(2);

        methodsContainer.addMethod(methodName); // throws method already exists error

        matcher = SyntaxVerifier.METHOD_ARGUMENT_DECLARATION.matcher(matcher.group(3));
        while (matcher.find()) {
            if (!SyntaxVerifier.isMethodArgument(matcher.group(0))) { // in case argument is illegal
                throw new InvalidSyntaxException();
            }
            boolean isFinal = matcher.group(2) != null;
            String varType = matcher.group(3);
            if (varType != null) {
                varType = varType.trim();
            }
            String varName = matcher.group(4);
            if (varName != null) {
                varName = varName.trim();
            }
            methodsContainer.addParameterToMethod(methodName, isFinal, varType, varName);
        }
    }

    /**
     * Parses a syntactically legal method scope statements.
     * Adds the method arguments to method table.
     * After done remove the method table from symbol table manager.
     *
     * @throws Exception in case of an Exception error
     */
    private void parseMethodStatement() throws Exception {
        symbolTableManager.createNewTable();
        addMethodParametersToTable();
        parseStatementsBlock(true);
        symbolTableManager.removeTable();
    }

    /**
     * Parses a syntactically legal scope statements.
     *
     * @param isMethod if the scope belong to a method
     * @throws Exception in case of an Exception error
     */
    private void parseStatementsBlock(boolean isMethod) throws Exception {
        boolean shouldNotAdvance = false;   // true in case we shouldn't advance the line
        // when entering the next iteration in the while loop.
        while (shouldNotAdvance || advanceLine()) {
            shouldNotAdvance = false;
            if (SyntaxVerifier.isClosingBracket(currentLine)) {
                if (!isMethod) { // end of if/while scope
                    return;
                } else {
                    throw new ParsingException(MISSING_RETURN_STATEMENT);
                }
            } else if (SyntaxVerifier.isVariableDeclaration(currentLine)) {
                parseVariableDeclaration();
            } else if (SyntaxVerifier.isVariableAssignment(currentLine)) {
                parseVariableAssignment();
            } else if (SyntaxVerifier.isMethodCallStatement(currentLine)) {
                parseMethodCallStatement();
            } else if (SyntaxVerifier.isIfOrWhileStatement(currentLine)) {
                parseIfOrWhileStatement();
            } else if (SyntaxVerifier.isReturnStatement(currentLine)) {
                if (isMethod) { // in case of return in if/while
                    advanceLine();
                    shouldNotAdvance = true;
                    if (SyntaxVerifier.isClosingBracket(currentLine)) {
                        return;
                    }
                }
            } else if (SyntaxVerifier.isMethodDeclaration(currentLine)) {
                throw new ParsingException(NESTED_METHOD_DECLARATION);
            } else {
                throw new InvalidSyntaxException();
            }
        }
    }

    /**
     * adds method parameters from declaration to the method symbol table
     *
     * @throws Exception in case of an Exception error
     */
    private void addMethodParametersToTable() throws Exception {
        Matcher matcher = SyntaxVerifier.METHOD_DECLARATION_PATTERN.matcher(currentLine);
        matcher.find();
        String currentMethod = matcher.group(2);

        List<Parameter> methodParameters = methodsContainer.getMethodParameterList(currentMethod);
        for (Parameter param : methodParameters) {
            boolean isFinal = param.isFinal();
            String varType = param.getType();
            String varName = param.getName();
            symbolTableManager.addVariable(varName, varType, true, isFinal);
        }
    }

    /**
     * Parses a syntactically legal if or while scope statements.
     *
     * @throws Exception in case of an Exception error
     */
    private void parseIfOrWhileStatement() throws Exception {
        symbolTableManager.createNewTable();
        Matcher matcher = SyntaxVerifier.IF_OR_WHILE_PATTERN.matcher(currentLine);
        matcher.find();
        String conditionalExpression = matcher.group(2);
        validateCondition(conditionalExpression);
        parseStatementsBlock(false);
        symbolTableManager.removeTable();
    }

    /**
     * check validity of condition in if or while statement
     *
     * @param line String representing the condition expression (inside parentheses)
     * @throws Exception in case of an Exception error
     */
    private void validateCondition(String line) throws Exception {
        if (line == null) {
            throw new InvalidSyntaxException(NON_BOOLEAN_VARIABLE_TYPE_IN_CONDITION_ERROR);
        }

        Matcher matcher = SyntaxVerifier.IF_OR_WHILE_IDENTIFIER.matcher(line);
        while (matcher.find()) {
            String expression = matcher.group(1);
            if (expression == null) {
                throw new ParsingException(ILLEGAL_CONDITIONAL_STATEMENT);
            }
            expression = expression.trim();
            String varType = getExpressionTypeAndVerifyVariableInitialization(expression);

            if (varType == null || !SyntaxVerifier.isTypeOf(varType, SyntaxVerifier.BOOLEAN)) {
                throw new InvalidSyntaxException(NON_BOOLEAN_VARIABLE_TYPE_IN_CONDITION_ERROR);
            }
        }
    }

    /**
     * Parses a syntactically legal method call statement.
     *
     * @throws InvalidSyntaxException in case of an InvalidSyntaxException error
     */
    private void parseMethodCallStatement() throws Exception {
        Matcher matcher = SyntaxVerifier.METHOD_CALL_PATTERN.matcher(currentLine);
        matcher.find();
        String methodName = matcher.group(1);

        boolean methodNotFound = !methodsContainer.containsMethod(methodName);
        if (methodNotFound) {
            throw new ParsingException(CALL_TO_NON_EXISTENT_METHOD_ERROR + methodName);
        }
        parseMethodCallArgumentsTypes(methodName, matcher);
    }

    /**
     * Parses a syntactically legal method call arguments type.
     *
     * @param methodName String representing the method name.
     * @param matcher    Matcher object initialized to the method call pattern
     * @throws ParsingException in case of an ParsingException error
     */
    private void parseMethodCallArgumentsTypes(String methodName, Matcher matcher) throws ParsingException {
        String argsString = matcher.group(2);

        String[] args = {};
        if (!argsString.isBlank()) {
            args = argsString.split(COMMA_REGEX);
        }

        List<Parameter> parameterList = methodsContainer.getMethodParameterList(methodName);

        if (parameterList.size() != args.length) {
            throw new ParsingException(NUMBER_OF_ARGUMENTS_TO_METHOD_CALL_ERROR);
        }

        for (int i = 0; i < args.length; i++) {
            String argType = getExpressionTypeAndVerifyVariableInitialization(args[i].trim());
            String paramType = parameterList.get(i).getType();
            if (!SyntaxVerifier.isTypeOf(paramType, argType)) {
                throw new ParsingException(METHOD_CALL_NOT_MATCH_DECLARATION);
            }
        }
    }


    /**
     * Returns the type a given expression represents, or null if the expression is invalid.
     * Expression may be a variable in the symbol table, in such case this method also
     * verify the variable is initialized. If not, it throws usage of uninitialized variable error.
     */
    private String getExpressionTypeAndVerifyVariableInitialization(String exp) throws ParsingException {
        if (SyntaxVerifier.isIdentifier(exp)) {
            Variable variable = symbolTableManager.getVariable(exp);
            if (variable == null || !variable.isInitialized()) {
                throw new ParsingException(USAGE_OF_UNINITIALIZED_VARIABLE);
            }
            return variable.getType();
        }

        return SyntaxVerifier.getExpressionType(exp);
    }


    /**
     * Verifies that the expression is legal, initialized (in case it's a variable), and
     * that its type matches requiredType.
     */
    private void verifyExpressionTypeAndInitialization(String requiredType, String expression)
            throws ParsingException {
        String expressionType = getExpressionTypeAndVerifyVariableInitialization(expression);
        if (expressionType == null) {
            throw new ParsingException(INVALID_EXPRESSION_ERROR + expression);
        }
        if (!SyntaxVerifier.isTypeOf(requiredType, expressionType)) {
            throw new ParsingException(ATTEMPTED_TO_ASSIGN_ERROR + expressionType + TO_STRING + requiredType);
        }
    }
}
