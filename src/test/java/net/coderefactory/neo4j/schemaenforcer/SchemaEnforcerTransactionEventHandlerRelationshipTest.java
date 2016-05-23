package net.coderefactory.neo4j.schemaenforcer;

import net.coderefactory.neo4j.schemaenforcer.schema.Schema;
import net.coderefactory.neo4j.schemaenforcer.schema.SchemaProvider;
import net.coderefactory.neo4j.schemaenforcer.validation.SchemaViolationException;
import net.coderefactory.neo4j.schemaenforcer.validation.Type;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link SchemaEnforcerTransactionEventHandler}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SchemaEnforcerTransactionEventHandlerRelationshipTest {

    public static final String FIELD = "field";

    private Schema schema;

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
        schema = new Schema.Builder().build();
        when(schemaProvider.getSchema(any(Relationship.class))).thenReturn(schema);

        schemaEnforcer.beforeCommit(data);
    }

    @Test
    public void testCommitOnValidSchema() throws Exception {
        schema = new Schema.Builder().property(FIELD, Type.String).build();
        when(schemaProvider.getSchema(any(Relationship.class))).thenReturn(schema);

        schemaEnforcer.beforeCommit(data);
    }

    @Test(expected = SchemaViolationException.class)
    public void testRollbackOnInValidSchema() throws Exception {
        schema = new Schema.Builder().property(FIELD, Type.Number).build();
        when(schemaProvider.getSchema(any(Relationship.class))).thenReturn(schema);

        schemaEnforcer.beforeCommit(data);
    }

}