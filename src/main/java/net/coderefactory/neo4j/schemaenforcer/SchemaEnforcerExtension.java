package net.coderefactory.neo4j.schemaenforcer;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.logging.LogService;
import org.neo4j.kernel.impl.logging.StoreLogService;
import org.neo4j.kernel.impl.spi.KernelContext;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.neo4j.kernel.lifecycle.LifecycleAdapter;
import org.neo4j.logging.Log;

import java.util.Objects;

/**
 * This class registers Schema Enforcer extension.
 * @author jstrzelecki
 */
public class SchemaEnforcerExtension extends KernelExtensionFactory<SchemaEnforcerExtension.Dependencies> {

    private static final String SCHEMA_ENFORCER_EXTENSION = "SCHEMA_ENFORCER_SERVICE";

    private TransactionEventHandler transactionEventHandler;

    public SchemaEnforcerExtension() {
        super(SCHEMA_ENFORCER_EXTENSION);
    }

    @Override
    public Lifecycle newInstance(final KernelContext context, final Dependencies dependencies) throws Throwable {
        final GraphDatabaseService graphDatabaseService = Objects.requireNonNull(dependencies.getGraphDatabaseService(), "GraphDatabaseService is required");
        final LogService logService = Objects.requireNonNull(dependencies.getStoreLogService(), "LogService is required");

        final Log log = logService.getUserLog(SchemaEnforcerExtension.class);

        return new LifecycleAdapter() {

            @Override
            public void init() throws Throwable {
                final SchemaProvider schemaProvider = new DbSchemaProvider(graphDatabaseService);
                transactionEventHandler = new SchemaEnforcerTransactionEventHandler(schemaProvider, logService);
                log.info(SCHEMA_ENFORCER_EXTENSION + " initialized");
            }

            @Override
            public void start() throws Throwable {
                graphDatabaseService.registerTransactionEventHandler(transactionEventHandler);
                log.info(SCHEMA_ENFORCER_EXTENSION + " started");
            }

            @Override
            public void stop() throws Throwable {
                graphDatabaseService.unregisterTransactionEventHandler(transactionEventHandler);
                log.info(SCHEMA_ENFORCER_EXTENSION + " stopped");
            }

            @Override
            public void shutdown() throws Throwable {
                transactionEventHandler = null;
            }
        };
    }

    public interface Dependencies {
        GraphDatabaseService getGraphDatabaseService();
        StoreLogService getStoreLogService();
    }
}
