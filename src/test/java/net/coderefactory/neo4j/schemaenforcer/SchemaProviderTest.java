package net.coderefactory.neo4j.schemaenforcer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.Collections;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.when;


/**
 * Tests for {@link DbSchemaProvider}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SchemaProviderTest {

    @Mock
    private GraphDatabaseService graphDatabaseService;

    @Mock
    private Node schemaNode, dateNode;

    private final Label nodeLabel = DynamicLabel.label("NodeType");

    private String[] schema = new String[]{"name:string", "price:number", "points:array[int]",
            "no_separator", "two_separators:foo:bar" };

    private SchemaProvider schemaProvider;

    @Before
    public void setUp() throws Exception {
        when(graphDatabaseService.findNode(DbSchemaProvider.METADATA, DbSchemaProvider.LABEL_PROPERTY, nodeLabel.name()))
                .thenReturn(schemaNode);
        when(schemaNode.getProperty(DbSchemaProvider.SCHEMA_PROPERTY)).thenReturn(schema);

        when(dateNode.getLabels()).thenReturn(Collections.singleton(nodeLabel));

        schemaProvider = new DbSchemaProvider(graphDatabaseService);
    }

    @Test
    public void testGetNodeSchema() throws Exception {
        final Map<String, String> nodeSchema = schemaProvider.getNodeSchema(dateNode);

        assertEquals("string", nodeSchema.get("name"));
        assertEquals("number", nodeSchema.get("price"));
        assertEquals("array[int]", nodeSchema.get("points"));

        assertNull(nodeSchema.get("no_separator"));
        assertNull(nodeSchema.get("two_separator"));
        assertNull(nodeSchema.get("not_defined"));
    }
}