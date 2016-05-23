package net.coderefactory.neo4j.schemaenforcer.schema;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;

public interface SchemaProvider {

    /**
     * Retrieves schema for given {@link Node} or {@link Relationship} of graph.
     *
     * @return map where key is the name of property and value type specifier.
     */
    Schema getSchema(final PropertyContainer propertyContainer);

}