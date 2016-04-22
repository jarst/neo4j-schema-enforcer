package net.coderefactory.neo4j.schemaenforcer;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.impl.logging.LogService;
import org.neo4j.logging.Log;

import java.util.Map;

/**
 * Transaction Event Handler enforcing node schema.
 */
public class SchemaEnforcerTransactionEventHandler extends TransactionEventHandler.Adapter {

    private final SchemaProvider schemaProvider;
    private final Log log;

    public SchemaEnforcerTransactionEventHandler(final SchemaProvider schemaProvider, final LogService logService) {
        this.schemaProvider = schemaProvider;
        log = logService.getUserLog(SchemaEnforcerTransactionEventHandler.class);
    }

    @Override
    public Object beforeCommit(final TransactionData data) throws Exception {
        log.info("beforeCommit");

        for (final PropertyEntry<Node> prop : data.assignedNodeProperties()) {
            final Map<String, String> nodeSchema = schemaProvider.getNodeSchema(prop.entity());

            final String key = prop.key();
            final String propType = nodeSchema.get(key);
            if (propType != null) {
                validateProperty(key, propType, prop.value());
            }
        }

        return null;
    }

    private void validateProperty(final String key, final String propType, final Object value) throws Exception {
        final PropertyTypeValidator propertyTypeValidator = PropertyTypeValidator.get(propType);
        if (propertyTypeValidator != null) {
            if (!propertyTypeValidator.isValid(value)) {
                rollback(key, propertyTypeValidator.getAllowedTypeName());
            }
        } else {
            log.warn("Unsupported property type:" + propType);
        }
    }

    private void rollback(final String property, final String type) throws Exception {
        final String msg = "Constraint violation: property '" + property + "' was not of type: " + type;
        log.error(msg);
        throw new Exception(msg);
    }

    @Override
    public void afterCommit(final TransactionData data, final Object state) {
        log.info("afterCommit");
    }
}
