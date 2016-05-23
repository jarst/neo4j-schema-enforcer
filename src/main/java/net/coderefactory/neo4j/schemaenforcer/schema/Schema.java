package net.coderefactory.neo4j.schemaenforcer.schema;

import net.coderefactory.neo4j.schemaenforcer.validation.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Schema {

    private final Map<String, Type> schema;

    public Schema() {
        this(new HashMap<String, Type>());
    }

    public Schema(final Map<String, Type> schema) {
        this.schema = Objects.requireNonNull(schema);
    }

    public boolean isDefined() {
        return !schema.isEmpty();
    }

    public Type getType(final String property) {
        return schema.get(property);
    }

    public void setType(final String property, final Type type) {
        schema.put(property, type);
    }
}
