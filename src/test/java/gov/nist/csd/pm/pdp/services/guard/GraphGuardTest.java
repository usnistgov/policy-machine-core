package gov.nist.csd.pm.pdp.services.guard;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.MemPAP;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.memory.MemObligations;
import gov.nist.csd.pm.pip.memory.MemPIP;
import gov.nist.csd.pm.pip.memory.MemProhibitions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.ALL_ADMIN_OPS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

class GraphGuardTest {

    private GraphGuard guard;
    private static final UserContext u1Ctx = new UserContext("u1");
    private static final UserContext u2Ctx = new UserContext("u2");
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

        guard = new GraphGuard(pap, new PReviewDecider(graph, pap.getProhibitions(), new OperationSet("read", "write")));
    }

    @Nested
    class CreatePolicyClass {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkCreatePolicyClass(superCtx));
        }

        @Test
        void testU1Error() {
            assertThrows(PMException.class, () -> guard.checkCreatePolicyClass(u1Ctx));
        }
    }

    @Nested
    class CreateNode {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkCreateNode(superCtx, OA, "oa1", new String[]{"oa2", "pc1_default_OA"}));
        }

        @Test
        void testU2CreateNode() {
            assertDoesNotThrow(() -> guard.checkCreateNode(u2Ctx, OA, "oa1", new String[]{"oa2"}));
        }

        @Test
        void testU1CreateNodeError() {
            assertThrows(PMException.class,
                    () -> guard.checkCreateNode(u1Ctx, OA, "oa1", null));
        }

    }

    @Nested
    class UpdateNode {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkUpdateNode(superCtx, "oa1"));
            assertDoesNotThrow(() -> guard.checkUpdateNode(superCtx, "pc1"));
            assertDoesNotThrow(() -> guard.checkUpdateNode(superCtx, "ua1"));
            assertDoesNotThrow(() -> guard.checkUpdateNode(superCtx, "u1"));
            assertDoesNotThrow(() -> guard.checkUpdateNode(superCtx, "o1"));

        }

        @Test
        void testU2UpdateOA1() {
            assertDoesNotThrow(() -> guard.checkUpdateNode(u2Ctx, "oa1"));
        }

        @Test
        void testU1UpdateOA1Error() {
            assertThrows(PMException.class, () -> guard.checkUpdateNode(u1Ctx, "oa1"));
        }


    }

    @Nested
    class DeleteNode {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkDeleteNode(superCtx, OA, "oa1"));
            assertDoesNotThrow(() -> guard.checkDeleteNode(superCtx, PC, "pc1"));
            assertDoesNotThrow(() -> guard.checkDeleteNode(superCtx, UA, "ua1"));
            assertDoesNotThrow(() -> guard.checkDeleteNode(superCtx, U, "u1"));
            assertDoesNotThrow(() -> guard.checkDeleteNode(superCtx, O, "o1"));

        }

        @Test
        void testU2UpdateOA1() {
            assertThrows(PMException.class, () -> guard.checkDeleteNode(u2Ctx, OA, "oa1"));
        }

        @Test
        void testU1UpdateOA1Error() {
            assertThrows(PMException.class, () -> guard.checkDeleteNode(u1Ctx, OA, "oa1"));
        }


    }

    @Nested
    class Exists {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkExists(superCtx, "pc1"));
            assertDoesNotThrow(() -> guard.checkExists(superCtx, "oa1"));
            assertDoesNotThrow(() -> guard.checkExists(superCtx, "ua1"));
            assertDoesNotThrow(() -> guard.checkExists(superCtx, "u1"));
            assertDoesNotThrow(() -> guard.checkExists(superCtx, "o1"));

        }

        @Test
        void testU2() {
            assertDoesNotThrow(() -> guard.checkExists(u2Ctx, "oa1"));
        }

        @Test
        void testU1Error() throws PMException {
            assertFalse(guard.checkExists(u1Ctx, "pc1"));
        }

    }

    @Nested
    class FilterStrings {

        @Test
        void testSuper() throws PMException {
            Set<String> nodes = new HashSet<>(Arrays.asList("pc1", "oa1", "ua1", "o1", "u1"));
            guard.filter(superCtx, nodes);
            assertEquals(5, nodes.size());
        }

        @Test
        void testU1() throws PMException {
            Set<String> nodes = new HashSet<>(Arrays.asList("oa1", "o1"));
            guard.filter(u1Ctx, nodes);
            assertEquals(2, nodes.size());
        }

        @Test
        void testU2() throws PMException {
            Set<String> nodes = new HashSet<>(Arrays.asList("oa1", "o1", "oa2"));
            guard.filter(u2Ctx, nodes);
            assertEquals(3, nodes.size());
        }

    }

    @Nested
    class FilterNodes {

        Set<Node> getNodes() throws PMException {
            Set<Node> nodes = guard.pap.getGraph().getNodes();
            nodes.removeIf(n -> !Arrays.asList("pc1", "oa1", "oa2", "o1", "ua1", "u1").contains(n.getName()));
            return nodes;
        }

        @Test
        void testSuper() throws PMException {
            Set<Node> nodes = getNodes();
            guard.filterNodes(superCtx, nodes);
            assertEquals(6, nodes.size());
        }

        @Test
        void testU1() throws PMException {
            Set<Node> nodes = getNodes();
            guard.filterNodes(u1Ctx, nodes);
            assertEquals(2, nodes.size());
        }

        @Test
        void testU2() throws PMException {
            Set<Node> nodes = getNodes();
            guard.filterNodes(u2Ctx, nodes);
            assertEquals(3, nodes.size());
        }

    }

    @Nested
    class FilterMap {

        @Test
        void testSuper() throws PMException {
            Map<String, OperationSet> assocs = guard.pap.getGraph().getSourceAssociations("ua2");
            guard.filter(superCtx, assocs);
            assertEquals(2, assocs.size());
        }

        @Test
        void testU1() throws PMException {
            Map<String, OperationSet> assocs = guard.pap.getGraph().getSourceAssociations("ua2");
            guard.filter(u1Ctx, assocs);
            assertEquals(1, assocs.size());
        }

        @Test
        void testU2() throws PMException {
            Map<String, OperationSet> assocs = guard.pap.getGraph().getSourceAssociations("ua2");
            guard.filter(u2Ctx, assocs);
            assertEquals(2, assocs.size());
        }

    }

    @Nested
    class Assign {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkAssign(superCtx, "u1", "ua2"));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkAssign(u1Ctx, "u1", "ua2"));
            assertThrows(PMException.class, () -> guard.checkAssign(u1Ctx, "oa1", "oa2"));
        }

        @Test
        void testU2() {
            assertThrows(PMException.class, () -> guard.checkAssign(u2Ctx, "u1", "ua2"));
            assertDoesNotThrow(() -> guard.checkAssign(u2Ctx, "oa1", "oa2"));
        }

    }

    @Nested
    class Deassign {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkDeassign(superCtx, "u2", "ua2"));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkDeassign(u1Ctx, "u1", "ua2"));
            assertThrows(PMException.class, () -> guard.checkDeassign(u1Ctx, "o1", "oa1"));
            assertThrows(PMException.class, () -> guard.checkDeassign(u1Ctx, "o1", "oa2"));
        }

        @Test
        void testU2() {
            assertThrows(PMException.class, () -> guard.checkDeassign(u2Ctx, "u1", "ua1"));
            assertThrows(PMException.class, () -> guard.checkDeassign(u2Ctx, "u2", "ua2"));
            assertDoesNotThrow(() -> guard.checkDeassign(u2Ctx, "o1", "oa2"));
        }

    }

    @Nested
    class Associate {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkAssociate(superCtx, "ua1", "oa2"));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkAssociate(u1Ctx, "ua1", "oa2"));
        }

        @Test
        void testU2() {
            assertThrows(PMException.class, () -> guard.checkAssociate(u2Ctx, "ua1", "oa2"));
        }

    }

    @Nested
    class Dissociate {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkDissociate(superCtx, "ua1", "oa1"));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkDissociate(u1Ctx, "ua1", "oa1"));
        }

        @Test
        void testU2() {
            assertThrows(PMException.class, () -> guard.checkDissociate(u2Ctx, "ua1", "oa1"));
        }

    }

    @Nested
    class GetAssociations {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkGetAssociations(superCtx, "ua1"));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkGetAssociations(u1Ctx, "ua1"));
        }

        @Test
        void testU2() {
            assertThrows(PMException.class, () -> guard.checkGetAssociations(u2Ctx, "ua1"));
        }

    }

    @Nested
    class Json {

        @Test
        void testSuper() {
            assertDoesNotThrow(() -> guard.checkToJson(superCtx));
        }

        @Test
        void testU1() {
            assertThrows(PMException.class, () -> guard.checkToJson(u1Ctx));
        }

        @Test
        void testU2() {
            assertThrows(PMException.class, () -> guard.checkToJson(u2Ctx));
        }

    }

}