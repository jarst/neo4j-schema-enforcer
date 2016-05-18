package net.coderefactory.neo4j.schemaenforcer;

import net.coderefactory.neo4j.schemaenforcer.validation.PropertiesValidator;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.impl.logging.LogService;
import org.neo4j.logging.Log;

/**
 * Transaction Event Handler enforcing node schema.
 */
public class SchemaEnforcerTransactionEventHandler extends TransactionEventHandler.Adapter {

    private final PropertiesValidator propertiesValidator;
    private final Log log;

    public SchemaEnforcerTransactionEventHandler(final SchemaProvider schemaProvider, final LogService logService) {
        this.propertiesValidator = new PropertiesValidator(schemaProvider, logService);
        log = logService.getUserLog(SchemaEnforcerTransactionEventHandler.class);
    }

    @Override
    public Object beforeCommit(final TransactionData data) throws Exception {
        log.info("beforeCommit");

        for (final PropertyEntry<Node> prop : data.assignedNodeProperties()) {
            propertiesValidator.validatePropertyEntry(prop);
        }
        for (final PropertyEntry<Relationship> prop : data.assignedRelationshipProperties()) {
            propertiesValidator.validatePropertyEntry(prop);
        }

        return null;
    }

    @Override
    public void afterCommit(final TransactionData data, final Object state) {
        log.info("afterCommit");
    }
}
