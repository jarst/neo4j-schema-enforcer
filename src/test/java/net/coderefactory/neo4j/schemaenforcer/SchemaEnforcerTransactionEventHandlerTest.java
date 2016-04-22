package net.coderefactory.neo4j.schemaenforcer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.kernel.impl.logging.StoreLogService;
import org.neo4j.logging.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link SchemaEnforcerTransactionEventHandler}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SchemaEnforcerTransactionEventHandlerTest {

    public static final String FIELD = "field";

    private Map<String, String> schema;

    @Mock
    private SchemaProvider schemaProvider;
    @Mock
    private StoreLogService logService;
    @Mock
    private Log log;
    @Mock
    private TransactionData data;

    private SchemaEnforcerTransactionEventHandler schemaEnforcer;

    @Before
    public void setUp(){
        schema = new HashMap<>();
        when(schemaProvider.getNodeSchema(any(Node.class))).thenReturn(schema);

        when(logService.getUserLog(any(Class.class))).thenReturn(log);

        schemaEnforcer = new SchemaEnforcerTransactionEventHandler(schemaProvider, logService);

        final PropertyEntry<Node> propertyEntry = new PropertyEntry<Node>() {
            @Override
            public Node entity() {
                return null;
            }
            @Override
            public Object previouslyCommitedValue() {
                return null;
            }
            @Override
            public String key() {
                return FIELD;
            }
            @Override
            public Object value() {
                return "The value is a string";
            }
        };
        when(data.assignedNodeProperties()).thenReturn(Collections.singleton(propertyEntry));
    }

    @Test
    public void testCommitOnNoSchema() throws Exception {
        schemaEnforcer.beforeCommit(data);
    }

    @Test
    public void testCommitOnValidSchema() throws Exception {
        schema.put(FIELD, PropertyTypeValidator.STRING);

        schemaEnforcer.beforeCommit(data);
    }

    @Test(expected = Exception.class)
    public void testRollbackOnInValidSchema() throws Exception {
        schema.put(FIELD, PropertyTypeValidator.NUMBER);

        schemaEnforcer.beforeCommit(data);
    }

}