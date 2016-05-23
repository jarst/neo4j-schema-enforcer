package net.coderefactory.neo4j.schemaenforcer.schema;

import net.coderefactory.neo4j.schemaenforcer.validation.Type;
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
        if (propertyContainer instanceof Node) {
            return getSchema((Node) propertyContainer);
        } else if (propertyContainer instanceof Relationship) {
            return getSchema((Relationship) propertyContainer);
        } else {
            return new Schema.Builder().build();
        }
    }

    private Schema getSchema(final Node node) {
        final Schema.Builder builder = new Schema.Builder();
        for (final Label label : node.getLabels()) {
            retrieveSchema(builder, label.name());
        }
        return builder.build();
    }

    private Schema getSchema(final Relationship relationship) {
        final Schema.Builder builder = new Schema.Builder();
        retrieveSchema(builder, relationship.getType().name());
        return builder.build();
    }

    private void retrieveSchema(final Schema.Builder builder, final String labelName) {
        final Node metaNode = graphDatabaseService.findNode(METADATA, LABEL_PROPERTY, labelName);
        if (metaNode != null) {
            final String[] schemaEntries = (String[]) metaNode.getProperty(SCHEMA_PROPERTY);
            for (final String entry : schemaEntries) {
                final String[] splitted = entry.split(PROP_NAME_TYPE_SEPARATOR);
                if (splitted.length == 2) {
                    final String propertyName = splitted[0].trim();
                    final String typeSpecifier = splitted[1].trim();

                    final Type propertyType = Type.getFor(typeSpecifier);
                    builder.property(propertyName, propertyType);
                }
            }
        }
    }

}
