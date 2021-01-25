package gov.nist.csd.pm.pdp.services.guard;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.MemPAP;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.MemGraph;
import gov.nist.csd.pm.pip.memory.MemObligations;
import gov.nist.csd.pm.pip.memory.MemPIP;
import gov.nist.csd.pm.pip.memory.MemProhibitions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.operations.Operations.ALL_ADMIN_OPS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObligationsGuardTest {

    private ObligationsGuard guard;
    private static final UserContext u1Ctx = new UserContext("u1");
    private static final UserContext superCtx = new UserContext("super");

    @BeforeEach
    void setUp() throws PMException {
        MemPAP pap = new MemPAP(new MemPIP(new MemGraph(), new MemProhibitions(), new MemObligations()));

        // create graph
        Graph graph = pap.getGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc1");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "pc1");
        graph.createNode("o1", O, null, "oa1", "oa2");
        graph.createNode("u1", U, null, "ua1");
        graph.createNode("u2", U, null, "ua2");

        graph.associate("ua1", "oa1", new OperationSet("read", "write"));
        graph.associate("ua2", "oa1", new OperationSet(ALL_ADMIN_OPS));
        graph.associate("ua2", "oa2", new OperationSet(ALL_ADMIN_OPS));

        guard = new ObligationsGuard(pap, new PReviewDecider(graph, pap.getProhibitions(), new OperationSet("read", "write")));
    }

    @Nested
    class checkAdd {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkAdd(superCtx));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkAdd(u1Ctx));
        }

    }

    @Nested
    class checkGet {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkGet(superCtx));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkGet(u1Ctx));
        }

    }

    @Nested
    class checkUpdate {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkUpdate(superCtx));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkUpdate(u1Ctx));
        }

    }

    @Nested
    class checkDelete {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkDelete(superCtx));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkDelete(u1Ctx));
        }

    }

    @Nested
    class checkEnable {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkEnable(superCtx));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkEnable(u1Ctx));
        }

    }
}
