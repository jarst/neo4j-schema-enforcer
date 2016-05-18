package net.coderefactory.neo4j.schemaenforcer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.TransactionFailureException;
import org.neo4j.test.TestGraphDatabaseFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class RelationshipSchemaIntegrationTest {

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
                    "            'title:string'\n" +
                    "        ]");
            graphDb.execute("CREATE (m:Metadata)\n" +
                    "    SET m.label = 'ReviewedBy',\n" +
                    "        m.schema = [\n" +
                    "            'rating:number',\n" +
                    "            'text:string'\n" +
                    "        ]");

            graphDb.execute("CREATE (b:Book) SET b.title = 'TheBook'");
            graphDb.execute("CREATE (p:Person) SET p.name = 'Alice'");
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
    public void testCanCreateValidRelationship() {
        Result result = null;
        try (Transaction tx = graphDb.beginTx()) {
            result = graphDb.execute("MATCH (b:Book{title:'TheBook'}), (p:Person{name:'Alice'})" +
                    " CREATE (b)-[r:ReviewedBy{rating:5, text:'Excellent!'}]->(p)" +
                    " RETURN r");
            tx.success();
        }

        assertNotNull(result);
        assertTrue(result.hasNext());
    }

    @Test(expected = TransactionFailureException.class)
    public void testCannotCreateInvalidRelationship() {
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.execute("MATCH (b:Book{title:'TheBook'}), (p:Person{name:'Alice'})" +
                    " CREATE (b)-[r:ReviewedBy{rating:'Good', text:3}]->(p)");
            tx.success();
        }

        fail("Transaction should have been rolled back.");
    }
}
