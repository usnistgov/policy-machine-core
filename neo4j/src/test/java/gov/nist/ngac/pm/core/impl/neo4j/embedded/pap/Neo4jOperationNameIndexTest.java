package gov.nist.ngac.pm.core.impl.neo4j.embedded.pap;

import static gov.nist.ngac.pm.core.impl.neo4j.embedded.pap.Neo4jTestInitializer.getTx;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

/**
 * Ticket 12's schema migration renamed {@code OPERATION_LABEL} from the legacy {@code "AdminOp"} to
 * {@code "Operation"}, which the existing {@code operation_name_index} was already declared against but
 * could never apply to while the label mismatched. This confirms a name lookup on the new label is now an
 * indexed seek, not an unindexed label scan.
 */
class Neo4jOperationNameIndexTest {

    @TempDir
    Path tempDir;

    @Test
    void testOperationNameLookupUsesIndex() {
        GraphDatabaseService graphDb = getTx(tempDir);

        try (Transaction tx = graphDb.beginTx()) {
            tx.createNode(Label.label("Operation")).setProperty("name", "assign");
            tx.commit();
        }

        try (Transaction tx = graphDb.beginTx()) {
            Result result = tx.execute(
                "PROFILE MATCH (n:Operation {name: $name}) RETURN n",
                Map.of("name", "assign"));
            result.resultAsString();

            String plan = result.getExecutionPlanDescription().toString();
            tx.commit();

            assertFalse(plan.contains("NodeByLabelScan"),
                "expected an indexed lookup by name on label Operation, not an unindexed label scan, got plan:\n" + plan);
        }
    }
}
