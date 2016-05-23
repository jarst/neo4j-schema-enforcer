package net.coderefactory.neo4j.schemaenforcer.validation;

import java.lang.reflect.Array;

/**
 * This class is responsible for validating single node properties.
 * @author jstrzelecki
 */
abstract public class PropertyTypeValidator {

    private final Type allowedType;
    private final Class[] allowedClasses;

    protected PropertyTypeValidator(final Type allowedType, final Class... innerClazz) {
        this.allowedType = allowedType;
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

    public Type getAllowedType() {
        return allowedType;
    }

    /** Checks if given value matches property type */
    public abstract boolean isValid(final Object value);

    /** Validator for primitive types. */
    static class SimpleType extends PropertyTypeValidator {
        SimpleType(final Type type, final Class... allowedClasses) {
            super(type, allowedClasses);
        }

        @Override
        public boolean isValid(final Object value) {
            return isTypeAllowed(value.getClass());
        }
    }

    /** Validator for <quote>collection</quote> types. */
    static class CollectionType extends PropertyTypeValidator {
        CollectionType(final Type type, final Class... allowedClasses) {
            super(type, allowedClasses);
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
