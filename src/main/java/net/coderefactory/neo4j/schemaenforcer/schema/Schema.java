package net.coderefactory.neo4j.schemaenforcer.schema;

import net.coderefactory.neo4j.schemaenforcer.validation.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Stores property schema.
 */
public class Schema {

    private final Map<String, Type> schema;

    private Schema(final Map<String, Type> schema) {
        this.schema = Objects.requireNonNull(schema);
    }

    public boolean isDefined() {
        return !schema.isEmpty();
    }

    public Type getType(final String property) {
        return schema.get(property);
    }

    /**
     * Builder for Schema.
     */
    public static class Builder {
        private Map<String, Type> typeMap = new HashMap<>();

        public Builder() { }

        public Builder property(final String name, final Type type) {
            typeMap.put(name, type);
            return this;
        }

        public Schema build () {
            return new Schema(typeMap);
        }
    }
}
