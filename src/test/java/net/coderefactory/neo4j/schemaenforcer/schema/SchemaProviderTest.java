package net.coderefactory.neo4j.schemaenforcer.schema;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.*;

import java.util.Collections;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Tests for {@link DbSchemaProvider}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SchemaProviderTest {

    private static final String TYPED_CONTAINER = "TypedContainer";

    @Mock
    private GraphDatabaseService graphDatabaseService;

    @Mock
    private Node schemaNode, dataNode;

    @Mock
    private Relationship relationship;

    private String[] schemaDefinition = new String[]{"name:string", "price:number", "points:array[int]",
            "no_separator", "two_separators:foo:bar" };

    private SchemaProvider schemaProvider;

    @Before
    public void setUp() throws Exception {
        when(graphDatabaseService.findNode(DbSchemaProvider.METADATA, DbSchemaProvider.LABEL_PROPERTY, TYPED_CONTAINER))
                .thenReturn(schemaNode);
        when(schemaNode.getProperty(DbSchemaProvider.SCHEMA_PROPERTY))
                .thenReturn(schemaDefinition);

        when(dataNode.getLabels()).thenReturn(Collections.singleton(DynamicLabel.label(TYPED_CONTAINER)));

        when(relationship.getType()).thenReturn(DynamicRelationshipType.withName(TYPED_CONTAINER));

        schemaProvider = new DbSchemaProvider(graphDatabaseService);
    }

    @Test
    public void testGetSchemaForNode() throws Exception {
        final Schema schema = schemaProvider.getSchema(dataNode);

        assertNotNull("Schema is returned", this.schemaDefinition);
        assertThat(schema.isDefined(), is(true));

        assertEquals("string", schema.getType("name"));
        assertEquals("number", schema.getType("price"));
        assertEquals("array[int]", schema.getType("points"));

        assertNull(schema.getType("no_separator"));
        assertNull(schema.getType("two_separator"));
        assertNull(schema.getType("not_defined"));
    }

    @Test
    public void testGetSchemaForRelationship() throws Exception {
        final Schema schema = schemaProvider.getSchema(relationship);

        assertNotNull("Schema is returned", schema);
        assertThat(schema.isDefined(), is(true));

        assertEquals("string", schema.getType("name"));
        assertEquals("number", schema.getType("price"));
        assertEquals("array[int]", schema.getType("points"));

        assertNull(schema.getType("no_separator"));
        assertNull(schema.getType("two_separator"));
        assertNull(schema.getType("not_defined"));
    }

    @Test
    public void handleUnsupportedPropertyContainer() {
        final PropertyContainer unsupportedContainer = mock(PropertyContainer.class);
        final Schema schema = schemaProvider.getSchema(unsupportedContainer);

        assertNotNull("Schema is returned", schema);
        assertThat("Schema for unsupported PropertyContainer is empty", schema.isDefined(), is(false));
    }
}