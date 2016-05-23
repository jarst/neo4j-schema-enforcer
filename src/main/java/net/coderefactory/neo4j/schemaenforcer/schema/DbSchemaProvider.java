package net.coderefactory.neo4j.schemaenforcer.schema;

import org.neo4j.graphdb.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class retrieves defined property types.
 * @author jstrzelecki
 */
public class DbSchemaProvider implements SchemaProvider {

    /** Nodes with this label are used for storage of node schemas. */
    public static final Label METADATA = DynamicLabel.label("Metadata");
    /** Name of property storing label. */
    public static final String LABEL_PROPERTY = "label";
    /** Name of property storing schema definition. */
    public static final String SCHEMA_PROPERTY = "schema";

    public static final String PROP_NAME_TYPE_SEPARATOR = ":";

    private final GraphDatabaseService graphDatabaseService;

    public DbSchemaProvider(final GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    /** {@inheritDoc} */
    @Override
    public Schema getSchema(final PropertyContainer propertyContainer) {
        return new Schema(getSchemaMap(propertyContainer));
    }

    private Map<String, String> getSchemaMap(final PropertyContainer propertyContainer) {
        if (propertyContainer instanceof Node) {
            return getSchema((Node) propertyContainer);
        } else if (propertyContainer instanceof Relationship) {
            return getSchema((Relationship) propertyContainer);
        } else {
            return Collections.emptyMap();
        }
    }

    private Map<String, String> getSchema(final Node node) {
        final Map<String, String> schema = new HashMap<>();
        for (final Label label : node.getLabels()) {
            retrieveSchema(schema, label.name());
        }
        return schema;
    }

    private Map<String, String> getSchema(final Relationship relationship) {
        final Map<String, String> schema = new HashMap<>();
        retrieveSchema(schema, relationship.getType().name());
        return schema;
    }

    private void retrieveSchema(Map<String, String> schema, String labelName) {
        final Node metaNode = graphDatabaseService.findNode(METADATA, LABEL_PROPERTY, labelName);
        if (metaNode != null) {
            final String[] schemaEntries = (String[]) metaNode.getProperty(SCHEMA_PROPERTY);
            for (final String entry : schemaEntries) {
                final String[] splitted = entry.trim().split(PROP_NAME_TYPE_SEPARATOR);
                if (splitted.length == 2) {
                    schema.put(splitted[0], splitted[1]);
                }
            }
        }
    }

}
