package net.coderefactory.neo4j.schemaenforcer.validation;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for validating single node properties.
 * @author jstrzelecki
 */
abstract public class PropertyTypeValidator {

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
        SimpleType(final String typeName, final Class... allowedClasses) {
            super(typeName, allowedClasses);
        }

        @Override
        public boolean isValid(final Object value) {
            return isTypeAllowed(value.getClass());
        }
    }

    /** Validator for <quote>collection</quote> types. */
    static class CollectionType extends PropertyTypeValidator {
        CollectionType(final String typeName, final Class... allowedClasses) {
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
