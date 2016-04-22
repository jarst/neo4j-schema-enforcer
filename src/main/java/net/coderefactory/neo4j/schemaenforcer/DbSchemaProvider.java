package net.coderefactory.neo4j.schemaenforcer;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

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
    public Map<String, String> getNodeSchema(final Node node) {
        final Map<String, String> nodeSchema = new HashMap<>(); //TODO introduce caching?

        for (final Label label : node.getLabels()) {
            final Node metaNode = graphDatabaseService.findNode(METADATA, LABEL_PROPERTY, label.name());
            if (metaNode != null) {
                final String[] schemaEntries = (String[]) metaNode.getProperty(SCHEMA_PROPERTY);
                for (final String entry : schemaEntries) {
                    final String[] splitted = entry.trim().split(PROP_NAME_TYPE_SEPARATOR);
                    if (splitted.length == 2) {
                        nodeSchema.put(splitted[0], splitted[1]);
                    }
                }
            }
        }
        return nodeSchema;
    }

}
