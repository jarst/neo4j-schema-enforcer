package net.coderefactory.neo4j.schemaenforcer;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
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
            validatePropertyEntry(prop);
        }
        for (final PropertyEntry<Relationship> prop : data.assignedRelationshipProperties()) {
            validatePropertyEntry(prop);
        }

        return null;
    }

    private void validatePropertyEntry(PropertyEntry<? extends PropertyContainer> prop) throws SchemaViolationException {
        final Map<String, String> schema = schemaProvider.getSchema(prop.entity());

        final String key = prop.key();
        final String propType = schema.get(key);
        if (propType != null) {
            validateProperty(key, propType, prop.value());
        }
    }

    private void validateProperty(final String key, final String propType, final Object value) throws SchemaViolationException {
        final PropertyTypeValidator propertyTypeValidator = PropertyTypeValidator.get(propType);
        if (propertyTypeValidator != null) {
            if (!propertyTypeValidator.isValid(value)) {
                rollback(key, propertyTypeValidator.getAllowedTypeName());
            }
        } else {
            log.warn("Unsupported property type:" + propType);
        }
    }

    private void rollback(final String property, final String type) throws SchemaViolationException {
        final String msg = "Constraint violation: property '" + property + "' was not of type: " + type;
        log.error(msg);
        throw new SchemaViolationException(msg);
    }

    @Override
    public void afterCommit(final TransactionData data, final Object state) {
        log.info("afterCommit");
    }
}
