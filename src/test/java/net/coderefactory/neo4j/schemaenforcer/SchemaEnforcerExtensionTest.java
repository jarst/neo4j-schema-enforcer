package net.coderefactory.neo4j.schemaenforcer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.impl.logging.LogService;
import org.neo4j.kernel.impl.logging.StoreLogService;
import org.neo4j.kernel.impl.spi.KernelContext;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.neo4j.logging.Log;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link SchemaEnforcerExtension}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SchemaEnforcerExtensionTest {

    @Mock
    private GraphDatabaseService graphDatabaseService;
    @Mock
    private StoreLogService logService;
    @Mock
    private Log log;
    @Mock
    private KernelContext context;

    private Lifecycle lifecycle;

    @Before
    public void setUp() throws Throwable {
        final SchemaEnforcerExtension schemaEnforcerExtension = new SchemaEnforcerExtension();
        when(logService.getUserLog(any(Class.class))).thenReturn(log);

        lifecycle = schemaEnforcerExtension.newInstance(context, new SchemaEnforcerExtension.Dependencies() {
            @Override
            public GraphDatabaseService getGraphDatabaseService() {
                return graphDatabaseService;
            }
            @Override
            public LogService getLogService() {
                return logService;
            }
        });

        lifecycle.init();
    }

    @Test
    public void testInit() throws Throwable {
        verifyNoMoreInteractions(graphDatabaseService);
    }

    @Test
    public void testStart() throws Throwable {
        lifecycle.start();

        verify(graphDatabaseService, only()).registerTransactionEventHandler(any(TransactionEventHandler.class));
    }

    @Test
    public void testStop() throws Throwable {
        lifecycle.stop();

        verify(graphDatabaseService, only()).unregisterTransactionEventHandler(any(TransactionEventHandler.class));
    }

    @Test
    public void testShutdown() throws Throwable {
        lifecycle.shutdown();

        verifyNoMoreInteractions(graphDatabaseService);
    }


}