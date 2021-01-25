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
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static gov.nist.csd.pm.operations.Operations.ALL_ADMIN_OPS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProhibitionsGuardTest {

    private ProhibitionsGuard guard;
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

        guard = new ProhibitionsGuard(pap, new PReviewDecider(graph, pap.getProhibitions(), new OperationSet("read", "write")));
    }

    @Nested
    class checkAdd {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkAdd(superCtx,
                    new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                            .addContainer("oa1", false)
                            .addContainer("oa2", true)
                            .build()
                    ));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkAdd(u1Ctx,
                    new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                            .addContainer("oa1", false)
                            .addContainer("oa2", true)
                            .build()
                    ));
        }
    }

    @Nested
    class checkGet {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkGet(superCtx,
                    new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                            .addContainer("oa1", false)
                            .addContainer("oa2", true)
                            .build()
                    ));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkGet(u1Ctx,
                    new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                            .addContainer("oa1", false)
                            .addContainer("oa2", true)
                            .build()
                    ));
        }
    }

    @Nested
    class checkUpdate {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkUpdate(superCtx,
                    new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                            .addContainer("oa1", false)
                            .addContainer("oa2", true)
                            .build()
                    ));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkUpdate(u1Ctx,
                    new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                            .addContainer("oa1", false)
                            .addContainer("oa2", true)
                            .build()
                    ));
        }
    }

    @Nested
    class checkDelete {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkDelete(superCtx,
                    new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                            .addContainer("oa1", false)
                            .addContainer("oa2", true)
                            .build()
                    ));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkDelete(u1Ctx,
                    new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                            .addContainer("oa1", false)
                            .addContainer("oa2", true)
                            .build()
                    ));
        }
    }

    @Nested
    class checkFilter {

        @Test
        void testSuper() {
            List<Prohibition> prohibitions = new ArrayList<>();
            prohibitions.add(new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                    .addContainer("oa1", false)
                    .addContainer("oa2", true)
                    .build()
            );

            guard.filter(superCtx, prohibitions);

            assertFalse(prohibitions.isEmpty());
        }

        @Test
        void testU1() {
            List<Prohibition> prohibitions = new ArrayList<>();
            prohibitions.add(new Prohibition.Builder("super-test", "ua1", new OperationSet("read"))
                    .addContainer("oa1", false)
                    .addContainer("oa2", true)
                    .build()
            );

            guard.filter(u1Ctx, prohibitions);

            assertTrue(prohibitions.isEmpty());
        }
    }

}
