package net.coderefactory.neo4j.schemaenforcer.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for property validators.
 */
public class PropertyTypeValidatorFactory {

    private static final Map<Type, PropertyTypeValidator> typeMap = new HashMap<>();

    static {
        typeMap.put(Type.Bool, new PropertyTypeValidator.SimpleType(Type.Bool, Boolean.class));
        typeMap.put(Type.Int, new PropertyTypeValidator.SimpleType(Type.Int, Long.class, Integer.class));
        typeMap.put(Type.Number, new PropertyTypeValidator.SimpleType(Type.Number, Number.class));
        typeMap.put(Type.String, new PropertyTypeValidator.SimpleType(Type.String, String.class));

        typeMap.put(Type.BoolArray, new PropertyTypeValidator.CollectionType(Type.BoolArray, boolean.class));
        typeMap.put(Type.IntArray, new PropertyTypeValidator.CollectionType(Type.IntArray, long.class, int.class));
        typeMap.put(Type.NumberArray, new PropertyTypeValidator.CollectionType(Type.NumberArray, double.class, float.class, long.class, int.class));
        typeMap.put(Type.StringArray, new PropertyTypeValidator.CollectionType(Type.StringArray, String.class));
    }

    /**
     * Gets validator for given property type.
     */
    public static PropertyTypeValidator get(final Type type) {
        return typeMap.get(type);
    }

}
