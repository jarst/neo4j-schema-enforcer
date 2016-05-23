package net.coderefactory.neo4j.schemaenforcer.validation;

public enum Type {

    Bool(Type.BOOL),
    Int(Type.INT),
    Number(Type.NUMBER),
    String(Type.STRING),
    BoolArray(Type.ARRAY_BOOL),
    IntArray(Type.ARRAY_INT),
    NumberArray(Type.ARRAY_NUMBER),
    StringArray(Type.ARRAY_STRING);

    private final String specifier;

    private Type(final String specifier) {
        this.specifier = specifier;
    }

    public String getSpecifier() {
        return specifier;
    }

    public String toString() {
        return specifier;
    }

    public static Type getFor(final String specifier) {
        for (Type type : values()) {
            if (type.specifier.equals(specifier)) {
                return type;
            }
        }
        return null;
    }

    public static final String BOOL = "bool";
    public static final String INT = "int";
    public static final String NUMBER = "number";
    public static final String STRING = "string";
    public static final String ARRAY_BOOL = "array[bool]";
    public static final String ARRAY_INT = "array[int]";
    public static final String ARRAY_NUMBER = "array[number]";
    public static final String ARRAY_STRING = "array[string]";
}
