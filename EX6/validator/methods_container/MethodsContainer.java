package oop.ex6.validator.methods_container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MethodsContainer class. responsible for holding all the information about methods declaration.
 */
public class MethodsContainer {
    /**
     * Constance representing the method overloading message error
     */
    private static final String METHOD_OVERLOADING_IS_NOT_SUPPORTED_ERROR =
            "Method overloading is not supported.";

    private final HashMap<String, List<Parameter>> methods;

    /**
     * MethodsContainer constructor.
     */
    public MethodsContainer() {
        this.methods = new HashMap<>();
    }

    /**
     * return true in case the container contains the given method.
     *
     * @param methodName String representing the method name
     * @return true, if method in container, false otherwise
     */
    public boolean containsMethod(String methodName) {
        return methods.containsKey(methodName);
    }

    /**
     * adds the given method to the map. takes only the method name, and initialize empty arguments list.
     *
     * @param methodName String representing the method name
     * @throws MethodsContainerException in case of overloading methods error
     */
    public void addMethod(String methodName) throws MethodsContainerException {
        boolean methodExists = containsMethod(methodName);
        if (methodExists) {
            throw new MethodsContainerException(METHOD_OVERLOADING_IS_NOT_SUPPORTED_ERROR);
        }
        methods.put(methodName, new ArrayList<>());
    }

    /***
     * returns the given method arguments list
     * @param methodName String representing the method name
     * @return List<List < String>>  representing the method arguments
     */
    public List<Parameter> getMethodParameterList(String methodName) {
        return methods.get(methodName);
    }

    /**
     * add a single parameter to the given method parameters list.
     * @param methodName name of method
     * @param isFinal is parameter final
     * @param paramType parameter type
     * @param paramName parameter name
     */
    public void addParameterToMethod(String methodName, boolean isFinal,
                                     String paramType, String paramName) {
        getMethodParameterList(methodName).add(new Parameter(paramName, paramType, isFinal));
    }
}
