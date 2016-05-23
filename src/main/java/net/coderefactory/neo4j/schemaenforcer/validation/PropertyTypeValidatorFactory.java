package net.coderefactory.neo4j.schemaenforcer.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for property validators.
 */
public class PropertyTypeValidatorFactory {

    private static final Map<String, PropertyTypeValidator> typeMap = new HashMap<>();

    static {
        typeMap.put(Type.BOOL, new PropertyTypeValidator.SimpleType(Type.BOOL, Boolean.class));
        typeMap.put(Type.INT, new PropertyTypeValidator.SimpleType(Type.INT, Long.class, Integer.class));
        typeMap.put(Type.NUMBER, new PropertyTypeValidator.SimpleType(Type.NUMBER, Number.class));
        typeMap.put(Type.STRING, new PropertyTypeValidator.SimpleType(Type.STRING, String.class));

        typeMap.put(Type.ARRAY_BOOL, new PropertyTypeValidator.CollectionType(Type.ARRAY_BOOL, boolean.class));
        typeMap.put(Type.ARRAY_INT, new PropertyTypeValidator.CollectionType(Type.ARRAY_INT, long.class, int.class));
        typeMap.put(Type.ARRAY_NUMBER, new PropertyTypeValidator.CollectionType(Type.ARRAY_NUMBER, double.class, float.class, long.class, int.class));
        typeMap.put(Type.ARRAY_STRING, new PropertyTypeValidator.CollectionType(Type.ARRAY_STRING, String.class));
    }

    /**
     * Gets validator for given property type.
     */
    public static PropertyTypeValidator get(final String type) {
        return typeMap.get(type);
    }

}
