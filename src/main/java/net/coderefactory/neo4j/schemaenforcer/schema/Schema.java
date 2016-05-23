package net.coderefactory.neo4j.schemaenforcer.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Schema {

    private final Map<String, String> schema;

    public Schema() {
        this(new HashMap<String, String>());
    }

    public Schema(final Map<String, String> schema) {
        this.schema = Objects.requireNonNull(schema);
    }

    public boolean isDefined() {
        return !schema.isEmpty();
    }

    public String getType(final String property) {
        return schema.get(property);
    }

    public void setType(final String property, final String type) {
        schema.put(property, type);
    }
}
