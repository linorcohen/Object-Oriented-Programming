package oop.ex6.validator.methods_container;

/**
 * Stores information about a method's parameter
 */
public class Parameter {
    private final String name;
    private final String type;
    private final boolean isFinal;

    /**
     * Constructor for Parameter object.
     * @param name parameter's name
     * @param type parameter's type
     * @param isFinal boolean value indicates if the parameter is final.
     */
    public Parameter(String name, String type, boolean isFinal) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
    }

    /**
     * Getter for the name.
     * @return parameter's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the type.
     * @return parameter's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for the parameter's final modifier.
     * @return true if variable is final, false otherwise.
     */
    public boolean isFinal() {
        return isFinal;
    }
}