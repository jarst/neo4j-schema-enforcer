package net.coderefactory.neo4j.schemaenforcer;

import net.coderefactory.neo4j.schemaenforcer.schema.SchemaProvider;
import net.coderefactory.neo4j.schemaenforcer.validation.PropertyTypeValidator;
import net.coderefactory.neo4j.schemaenforcer.validation.SchemaViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
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
public class SchemaEnforcerTransactionEventHandlerRelationshipTest {

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
        when(schemaProvider.getSchema(any(Node.class))).thenReturn(schema);

        when(logService.getUserLog(any(Class.class))).thenReturn(log);

        schemaEnforcer = new SchemaEnforcerTransactionEventHandler(schemaProvider, logService);

        when(data.assignedNodeProperties()).thenReturn(Collections.<PropertyEntry<Node>>emptyList());

        final PropertyEntry<Relationship> relationshipPropertyEntry = new PropertyEntry<Relationship>(){
            @Override
            public Relationship entity() {
                return null;
            }
            @Override
            public String key() {
                return FIELD;
            }
            @Override
            public Object previouslyCommitedValue() {
                return null;
            }
            @Override
            public Object value() {
                return "Valid string";
            }
        };
        when(data.assignedRelationshipProperties()).thenReturn(Collections.singleton(relationshipPropertyEntry));
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

    @Test(expected = SchemaViolationException.class)
    public void testRollbackOnInValidSchema() throws Exception {
        schema.put(FIELD, PropertyTypeValidator.NUMBER);

        schemaEnforcer.beforeCommit(data);
    }

}