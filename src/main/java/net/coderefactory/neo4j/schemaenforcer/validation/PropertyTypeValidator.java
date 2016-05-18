package net.coderefactory.neo4j.schemaenforcer.validation;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for validating single node properties.
 * @author jstrzelecki
 */
abstract public class PropertyTypeValidator {

    public static final String BOOL = "bool";
    public static final String INT = "int";
    public static final String NUMBER = "number";
    public static final String STRING = "string";
    public static final String ARRAY_BOOL = "array[bool]";
    public static final String ARRAY_INT = "array[int]";
    public static final String ARRAY_NUMBER = "array[number]";
    public static final String ARRAY_STRING = "array[string]";

    private static Map<String, PropertyTypeValidator> typeMap = new HashMap<>();

    static {
        typeMap.put(BOOL, new SimpleType(BOOL, Boolean.class));
        typeMap.put(INT, new SimpleType(INT, Long.class, Integer.class));
        typeMap.put(NUMBER, new SimpleType(NUMBER, Number.class));
        typeMap.put(STRING, new SimpleType(STRING, String.class));

        typeMap.put(ARRAY_BOOL, new CollectionType(ARRAY_BOOL, boolean.class));
        typeMap.put(ARRAY_INT, new CollectionType(ARRAY_INT, long.class, int.class));
        typeMap.put(ARRAY_NUMBER, new CollectionType(ARRAY_NUMBER, double.class, float.class, long.class, int.class));
        typeMap.put(ARRAY_STRING, new CollectionType(ARRAY_STRING, String.class));
    }

    /**
     * Gets validator for given property type.
     */
    public static PropertyTypeValidator get(final String type) {
        return typeMap.get(type);
    }


    private final String allowedTypeName;
    private final Class[] allowedClasses;

    protected PropertyTypeValidator(final String allowedTypeName, final Class... innerClazz) {
        this.allowedTypeName = allowedTypeName;
        this.allowedClasses = innerClazz;
    }

    protected boolean isTypeAllowed(final Class clazz) {
        for (Class<?> allowedClass : allowedClasses) {
            if (allowedClass.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    public String getAllowedTypeName() {
        return allowedTypeName;
    }

    /** Checks if given value matches property type */
    public abstract boolean isValid(final Object value);

    /** Validator for primitive types. */
    static class SimpleType extends PropertyTypeValidator {
        public SimpleType(final String typeName, final Class... allowedClasses) {
            super(typeName, allowedClasses);
        }

        @Override
        public boolean isValid(final Object value) {
            return isTypeAllowed(value.getClass());
        }
    }

    /** Validator for <quote>collection</quote> types. */
    static class CollectionType extends PropertyTypeValidator {
        public CollectionType(final String typeName, final Class... allowedClasses) {
            super(typeName, allowedClasses);
        }

        @Override
        public boolean isValid(final Object value) {
            final Class valueClazz = value.getClass();
            if (valueClazz.isArray()) {
                if (Array.getLength(value) == 0) {
                    return true; // No need to enforce type for empty arrays
                } else {
                    final Class componentType = valueClazz.getComponentType();
                    return isTypeAllowed(componentType);
                }
            } else {
                return false;
            }
        }
    }

}
