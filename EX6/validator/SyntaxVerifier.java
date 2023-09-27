package oop.ex6.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for verifying the correctness of lines in terms of
 * the language's grammar and structure.
 * Contains useful regex patterns and methods that can be used to verify and parse code.
 */
public class SyntaxVerifier {

    /**
     * Type Constants:
     */
    private static final String INT = "int";
    private static final String DOUBLE = "double";
    public static final String BOOLEAN = "boolean";
    private static final String STRING = "String";
    private static final String CHAR = "char";

    /**
     * Type Patterns:
     */
    private static final Pattern INT_VALUE = Pattern.compile("([+-]?\\d+)");
    private static final Pattern DOUBLE_VALUE = Pattern.compile("([+-]?(\\d+.?\\d*|\\d*.?\\d+))");
    private static final Pattern STRING_VALUE = Pattern.compile("(\"[^,'\\\\]*\")");
    private static final Pattern CHAR_VALUE = Pattern.compile("('[^,'\\\\]')");
    private static final Pattern BOOLEAN_VALUE = Pattern.compile("(false|true)");

    /**
     * Any legal value of unknown type (e.g. "hello world!", identifier, etc.
     * Used for parameter lists, etc.
     */
    private static final Pattern GENERIC_VALUE = Pattern.compile("([^\\s,][^,]*+)");

    /**
     * Any sequence (length > 0) of letters (uppercase or lowercase), digits and the underscore character
     * ( ). name may not start with a digit. An identifier may start with an underscore, but in such a case
     * it must contain at least one more character.
     */
    private static final String IDENTIFIER = "([a-zA-Z]\\w*|[a-zA-Z_]\\w+)";
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile(IDENTIFIER);

    /**
     * Can be used to tokenize a legal variable declaration and assignment expressions
     * with Matcher's find where:
     * Group 1 == name,
     * group 3 == value (including " for String, etc.) or null for uninitialized variables.
     */
    public static final Pattern VARS_DECLARATION_PATTERN = Pattern.compile(
            IDENTIFIER + "(\\s*+=\\s*+([^\\s,][^,]*))?\\s*+[,;]");

    private static final String VARIABLE_ASSIGNMENT = String.format(
            "\\s*%s\\s*+=\\s*+([^\\s,][^,]*)+(\\s*+,\\s*+%s\\s*+=\\s*+([^\\s,][^,]*)+)*\\s*;\\s*",
            IDENTIFIER, IDENTIFIER);
    private static final Pattern VARIABLE_ASSIGNMENT_PATTERN = Pattern.compile(VARIABLE_ASSIGNMENT);

    /**
     * Matches the start of a variable declaration, e.g. "final int".
     */
    public static final Pattern MODIFIER_AND_TYPE =
            Pattern.compile("^\\s*(final\\s)?\\s*+(int|boolean|double|char|String)");

    /**
     * Matches variable declaration after modifier and type, e.g. "foo = 4, bar;".
     */
    private static final Pattern VAR_DECLARATION_PATTERN =
            Pattern.compile("(" + IDENTIFIER + "\\s*+(=\\s*+[^\\s,][^,]*)?(\\s*,\\s*|\\s*;\\s*$))+");

    /**
     * Method call pattern: 'identifier([parameter list]);'
     * Does not verify validity of parameter list.
     * Can be used to extract parameter list with group 2.
     */
    public static final Pattern METHOD_CALL_PATTERN = Pattern.compile(
            "\\s*+" + IDENTIFIER + "\\s*+\\((.*)\\)\\s*+;\\s*+");

    private static final Pattern METHOD_CALL_PARAM_LIST = Pattern.compile(
            "\\s*+(" + GENERIC_VALUE + "(,\\s*+" + GENERIC_VALUE + ")*+)?\\s*+");

    private static final Pattern CLOSING_BRACKET_PATTERN = Pattern.compile("^\\s*+}\\s*+$");

    public static final Pattern OPENING_BRACKET_PATTERN = Pattern.compile("\\s*+\\{\\s*+$");

    /**
     * Matches method declaration prefix (return type and identifier), .e.g. void foo.
     */
    public static final Pattern METHOD_DECLARATION_PREFIX = Pattern.compile(
            "\\s*+(void\\s+)" + IDENTIFIER + "\\s*+");

    public static final Pattern IF_OR_WHILE_IDENTIFIER = Pattern.compile(
            "(" + BOOLEAN_VALUE + "|" + IDENTIFIER + "|" + INT_VALUE + "|" + DOUBLE_VALUE + ")");

    private static final Pattern IF_OR_WHILE_BOOLEAN_STATEMENT = Pattern.compile(
            IF_OR_WHILE_IDENTIFIER + "\\s*+((\\|\\||&&)+\\s*+" + IF_OR_WHILE_IDENTIFIER + "+\\s*+)*+");

    public static final Pattern IF_OR_WHILE_PATTERN = Pattern.compile(
            "\\s*+(if|while)\\s*+\\(\\s*+(" + IF_OR_WHILE_BOOLEAN_STATEMENT
                    + ")?\\s*+\\)\\s*+\\s*+\\{\\s*+");

    public static final Pattern METHOD_ARGUMENT_DECLARATION = Pattern.compile(
            "(" + "\\s*+(final\\s++)?\\s*+(int|boolean|double|char|String)\\s*+" + IDENTIFIER + ")");

    /**
     * Matches method declaration parameter list: (final)? type identifier(, ...)
     * e.g.: final int foo, char bar.
     */
    private static final Pattern METHOD_DECLARATION_PARAM_LIST = Pattern.compile(
            "(\\s*+" + METHOD_ARGUMENT_DECLARATION + "\\s*+" + "(\\s*+," +
                    METHOD_ARGUMENT_DECLARATION + ")*+\\s*+)?");

    public static final Pattern METHOD_DECLARATION_PATTERN = Pattern.compile(
            METHOD_DECLARATION_PREFIX + "\\(\\s*+(" + METHOD_DECLARATION_PARAM_LIST +
                    ")?\\)\\s*+\\s*+\\{\\s*+");

    private static final Pattern RETURN_STATEMENT_PATTERN = Pattern.compile("\\s*+return\\s*+;\\s*+");
    private static final String COMMENT_REGEX = "^//.*";


    /**
     * SyntaxVerifier Constractor.
     */
    private SyntaxVerifier() {
    }

    /**
     * return if the given line is a variable assignment.
     *
     * @param line string to evaluate.
     * @return true iff line is a valid variable(s) assignment.
     */
    public static boolean isVariableAssignment(String line) {
        return VARIABLE_ASSIGNMENT_PATTERN.matcher(line).matches();
    }

    /**
     * return if the given line is a variable declaration.
     *
     * @param line string to evaluate.
     * @return true iff line is a valid variable(s) declaration.
     */
    public static boolean isVariableDeclaration(String line) {
        Matcher matcher = MODIFIER_AND_TYPE.matcher(line);
        if (!matcher.find()) {
            return false;
        }
        line = line.substring(matcher.end()).trim();
        return VAR_DECLARATION_PATTERN.matcher(line).matches();
    }

    /**
     * return if the given line is a method declaration.
     *
     * @param line string to evaluate.
     * @return true iff line is a valid method declaration.
     */
    public static boolean isMethodDeclaration(String line) {
        return METHOD_DECLARATION_PATTERN.matcher(line).matches();
    }

    /**
     * Checks if the line is a legal closing bracket line.
     *
     * @param line The line to analyze.
     * @return true iff the line is a legal closing bracket line.
     */
    public static boolean isClosingBracket(String line) {
        return CLOSING_BRACKET_PATTERN.matcher(line).matches();
    }

    /**
     * return if the given line is a return statement.
     *
     * @param line string to evaluate.
     * @return true iff line is a valid return statement.
     */
    public static boolean isReturnStatement(String line) {
        return RETURN_STATEMENT_PATTERN.matcher(line).matches();
    }

    /**
     * return if the given line is if or while statement.
     *
     * @param line string to evaluate.
     * @return true iff line is a valid if or while statement.
     */
    public static boolean isIfOrWhileStatement(String line) {
        return IF_OR_WHILE_PATTERN.matcher(line).matches();
    }

    /**
     * return if the given line is a method call statement.
     *
     * @param line string to evaluate.
     * @return true iff line is a valid method call statement.
     */
    public static boolean isMethodCallStatement(String line) {
        Matcher matcher = METHOD_CALL_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return false;
        }
        // get the expression inside parentheses
        String args = matcher.group(2);
        return METHOD_CALL_PARAM_LIST.matcher(args).matches();
    }

    /**
     * @param line string to evaluate.
     * @return true iff s is a valid identifier.
     */
    public static boolean isIdentifier(String line) {
        return (IDENTIFIER_PATTERN.matcher(line).matches()
                && !BOOLEAN_VALUE.matcher(line).matches());
    }

    /**
     * return if the given line is a method argument.
     *
     * @param line string to evaluate.
     * @return true iff line is a valid method argument.
     */
    public static boolean isMethodArgument(String line) {
        return METHOD_ARGUMENT_DECLARATION.matcher(line).matches();
    }

    /**
     * return true in case the line is redundant meaning - a comment or empty
     *
     * @return true if redundant, false otherwise
     */
    public static boolean isRedundant(String line) {
        if (line.matches(COMMENT_REGEX)) {
            return true;
        }
        line = line.trim();
        return line.isEmpty();
    }

    /**
     * Returns the type of expression, or null if expression is invalid.
     * For example,
     * getExpressionType(""3"") -> "String",
     * getExpressionType(".4") -> "double"
     *
     * @param exp The expression to evaluate.
     * @return type of expression, or null if expression is invalid
     */
    public static String getExpressionType(String exp) {
        if (BOOLEAN_VALUE.matcher(exp).matches()) {
            return BOOLEAN;
        } else if (INT_VALUE.matcher(exp).matches()) {
            return INT;
        } else if (DOUBLE_VALUE.matcher(exp).matches()) {
            return DOUBLE;
        } else if (STRING_VALUE.matcher(exp).matches()) {
            return STRING;
        } else if (CHAR_VALUE.matcher(exp).matches()) {
            return CHAR;
        }
        return null;
    }

    /**
     * returns true if srcType can be cast to targetType.
     *
     * @param srcType    The type to cast.
     * @param targetType The type to be cast to.
     * @return boolean value indicating if casting is possible.
     */
    public static boolean isTypeOf(String srcType, String targetType) {
        switch (srcType) {
            case (STRING):
                return targetType.equals(STRING);
            case (CHAR):
                return targetType.equals(CHAR);
            case (BOOLEAN):
                return targetType.equals(BOOLEAN) || targetType.equals(INT) || targetType.equals(DOUBLE);
            case (INT):
                return targetType.equals(INT) || targetType.equals(BOOLEAN);
            case (DOUBLE):
                return targetType.equals(DOUBLE) || targetType.equals(INT) || targetType.equals(BOOLEAN);
            default:
                return false;
        }
    }
}
