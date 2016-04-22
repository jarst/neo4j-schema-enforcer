package net.coderefactory.neo4j.schemaenforcer;

import org.neo4j.graphdb.Node;

import java.util.Map;

public interface SchemaProvider {

    /**
     * Retrieves schema for given {@link Node} of graph.
     * @return map where key is the name of property and value type specifier.
     */
    Map<String, String> getNodeSchema(final Node node);
}
