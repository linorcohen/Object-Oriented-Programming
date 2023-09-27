import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/*
This test verifies that the ChatterBot class has all requested fields with the appropriate modifiers
and all requested non-private methods with the appropriate signatures.
 */
public class PreSubmit {
    private static final int NAME_INDEX = 0;
    private static final int RETURN_TYPE_INDEX = 1;
    private static final int PARAMETER_TYPES_INDEX = 2;
    public static final String[] CONSTANT_FIELDS = {
            "REQUEST_PREFIX",
            "REQUESTED_PHRASE_PLACEHOLDER",
            "ILLEGAL_REQUEST_PLACEHOLDER"
    };
    //public static final String[][] PUBLIC_METHODS = {
    //        {"ChatterBot", "ChatterBot", "[java.lang.String, java.lang.String[], java.lang.String[]]"},
    //        {"getName", "java.lang.String", "[]"},
    //        {"replyTo", "java.lang.String", "[java.lang.String]"}
    //};

    public static final String[][] PUBLIC_METHODS = {
            {"getName", "java.lang.String", "[]"},
            {"replyTo", "java.lang.String", "[class java.lang.String]"}
    };
    public static final String[] CONSTRUCTOR = {
            "ChatterBot", "ChatterBot", "[class java.lang.String, class [Ljava.lang.String;, class [Ljava.lang.String;]"
    };

    public static void main(String[] args) {
        verifyFields();
        verifyExecutables();
    }

    private static void verifyFields() {
        boolean found;
        int mod;

        Field[] fields = ChatterBot.class.getDeclaredFields();

        for (String name : CONSTANT_FIELDS) {
            found = false;
            for (Field field : fields) {
                String fieldName = field.getName();
                if (fieldName.equals(name)) {
                    found = true;
                    mod = field.getModifiers();
                    if (!Modifier.isStatic(mod))
                        System.out.printf("%s_not_static%n", name);
                    if (!Modifier.isFinal(mod))
                        System.out.printf("%s_not_final%n", name);
                    if (Modifier.isPrivate(mod))
                        System.out.printf("%s_private%n", name);
                }
            }
            if (!found) {
                System.out.printf("%s_not_found%n", name);
            }
        }
    }

    //private static void verifyExecutables() {
    //    boolean found;
    //    int mod;
    //    String returnType;
    //    String parameterTypes;

    //    Executable[] constructors = ChatterBot.class.getDeclaredConstructors();
    //    Executable[] methods = ChatterBot.class.getDeclaredMethods();
    //    Executable[] executables = new Executable[constructors.length + methods.length];
    //    System.arraycopy(constructors, 0, executables, 0, constructors.length);
    //    System.arraycopy(methods, 0, executables, constructors.length, methods.length);

    //    for (String[] methodDetails : PUBLIC_METHODS) {
    //        String name = methodDetails[NAME_INDEX];
    //        found = false;
    //        for (Executable executable : executables) {
    //            String execName = executable.getName();
    //            if (execName.equals(name)) {
    //                found = true;
    //                mod = executable.getModifiers();
    //                if (Modifier.isPrivate(mod))
    //                    System.out.printf("%s_private%n", name);
    //                returnType = executable.getAnnotatedReturnType().toString();
    //                if (!returnType.equals(methodDetails[RETURN_TYPE_INDEX])) {
    //                    System.out.printf("%s_wrong_return_type%n", name);
    //                }
    //                parameterTypes = Arrays.toString(executable.getAnnotatedParameterTypes());
    //                if (!parameterTypes.equals(methodDetails[PARAMETER_TYPES_INDEX])) {
    //                    System.out.printf("%s_wrong_parameter_types%n", name);
    //                }
    //            }
    //        }
    //        if (!found) {
    //            System.out.printf("%s_not_found%n", name);
    //        }
    //    }
    //}


   private static void verifyExecutables() {
        boolean found;
        int mod;
        String returnType;
        String parameterTypes;

        Constructor<?>[] constructors = ChatterBot.class.getDeclaredConstructors();
        Method[] methods = ChatterBot.class.getDeclaredMethods();

        for (String[] methodDetails : PUBLIC_METHODS) {
            String name = methodDetails[NAME_INDEX];
            found = false;
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.equals(name)) {
                    found = true;
                    mod = method.getModifiers();
                    if (Modifier.isPrivate(mod))
                        System.out.printf("%s_private%n", name);
                    returnType = method.getReturnType().getCanonicalName();
                    if (!returnType.equals(methodDetails[RETURN_TYPE_INDEX])) {
                        System.out.printf("%s_wrong_return_type%n", name);
                        System.out.printf("returnType:%s%n", returnType);
                        System.out.printf("methodDetails[RETURN_TYPE_INDEX]:%s%n", methodDetails[RETURN_TYPE_INDEX]);
                    }
                    parameterTypes = Arrays.toString(method.getParameterTypes());
                    if (!parameterTypes.equals(methodDetails[PARAMETER_TYPES_INDEX])) {
                        System.out.printf("%s_wrong_parameter_types%n", name);
                        System.out.printf("parameterTypes:%s%n", parameterTypes);
                        System.out.printf("methodDetails[PARAMETER_TYPES_INDEX]:%s%n", methodDetails[PARAMETER_TYPES_INDEX]);
                    }
                }
            }
            if (!found) {
                System.out.printf("%s_not_found%n", name);
            }
        }
        found = false;
        for (Constructor<?> constructor : constructors) {
            String constructorName = constructor.getName();
            if (constructorName.equals(CONSTRUCTOR[NAME_INDEX])) {
                found = true;
                mod = constructor.getModifiers();
                if (Modifier.isPrivate(mod))
                    System.out.printf("%s_private%n", CONSTRUCTOR[NAME_INDEX]);
                parameterTypes = Arrays.toString(constructor.getParameterTypes());
                if (!parameterTypes.equals(CONSTRUCTOR[PARAMETER_TYPES_INDEX])) {
                    System.out.printf("%s_wrong_parameter_types%n", CONSTRUCTOR[NAME_INDEX]);
                    System.out.printf("parameterTypes:%s%n", parameterTypes);
                    System.out.printf("methodDetails[PARAMETER_TYPES_INDEX]:%s%n", CONSTRUCTOR[PARAMETER_TYPES_INDEX]);
                }
            }
        }
        if (!found) {
            System.out.printf("%s_not_found%n", CONSTRUCTOR[NAME_INDEX]);
        }
    }
}
