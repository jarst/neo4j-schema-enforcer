package net.coderefactory.neo4j.schemaenforcer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.test.TestGraphDatabaseFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test {@link SchemaEnforcerExtension} with embedded database.
 */
public class IntegrationWithEmbeddedNeo4jTest {

    private GraphDatabaseService graphDb;

    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        createSchema();
    }

    private void createSchema() {
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.execute("CREATE (m:Metadata)\n" +
                    "    SET m.label = 'Book',\n" +
                    "        m.schema = [\n" +
                    "            'title:string',\n" +
                    "            'price:number',\n" +
                    "            'pages:int',\n" +
                    "            'available:bool',\n" +
                    "            'reviews:array[string]'\n" +
                    "        ]");
            tx.success();
        } catch (TransactionFailureException e) {
            fail("Failed to create schema.");
        }
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    @Test
    public void testCanCreateValidNode() {
        Result result = null;
        try (Transaction tx = graphDb.beginTx()) {
            result = graphDb.execute("CREATE (b:Book) " +
                    "SET b.title = 'The Book', b.price = 9.99, b.reviews = ['Best book ever!', 'I recommend it!']" +
                    "RETURN b");
            tx.success();
        } catch (TransactionFailureException e) {
            fail("Transaction should be committed successfully.");
        }

        assertNotNull(result);
        assertTrue(result.hasNext());
    }

    @Test(expected = TransactionFailureException.class)
    public void testCannotCreateInvalidNode() {
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.execute("CREATE (b:Book) " +
                    "SET b.title = 'Yet Another Book', b.price = 'free', b.pages = 3.14159, b.reviews = [2,3,1]");
            tx.success();
        }

        fail("Transaction should have been rolled back.");
    }
}
