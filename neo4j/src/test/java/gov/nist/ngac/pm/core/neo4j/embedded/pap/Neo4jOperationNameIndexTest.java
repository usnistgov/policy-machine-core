/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST.
 *
 * This file is part of the policy-machine-core-neo4j module, which compiles against
 * and embeds org.neo4j:neo4j (GPLv3, Community Edition). As a combined work, this
 * module is distributed under the GNU General Public License v3.0, not the CC BY 4.0
 * license used elsewhere in this repository. See neo4j/LICENSE for the full text.
 */

package gov.nist.ngac.pm.core.neo4j.embedded.pap;

import static gov.nist.ngac.pm.core.neo4j.embedded.pap.Neo4jTestInitializer.getTx;
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
