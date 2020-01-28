package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PReviewDeciderTest {

    private static long getID() {
        return new Random().nextLong();
    }

    @Test
    void testHasPermissions() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());
        Node o2 = graph.createNode(getID(), "o2", O, null, oa1.getID());
        Node o3 = graph.createNode(getID(), "o3", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.check(u1.getID(), 0, o1.getID(), "read", "write"));
    }

    @Test
    void testFilter() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());
        Node o2 = graph.createNode(getID(), "o2", O, null, oa1.getID());
        Node o3 = graph.createNode(getID(), "o3", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));
        Set<Node> nodes = graph.getNodes();

        PReviewDecider decider = new PReviewDecider(graph);
        assertEquals(new HashSet<>(Arrays.asList(o1, o2, o3, oa1)),
                new HashSet<>(decider.filter(u1.getID(), 0, nodes, "read"))
        );
    }

    @Test
    void testGetChildren() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());
        Node o2 = graph.createNode(getID(), "o2", O, null, oa1.getID());
        Node o3 = graph.createNode(getID(), "o3", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        Set<Node> children = decider.getChildren(u1.getID(), 0, oa1.getID());
        assertEquals(
                new HashSet<>(Arrays.asList(o1, o2, o3)),
                children
        );
    }

    @Test
    void testGetAccessibleNodes() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());
        Node o2 = graph.createNode(getID(), "o2", O, null, oa1.getID());
        Node o3 = graph.createNode(getID(), "o3", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        Map<Long, Set<String>> accessibleNodes = decider.getAccessibleNodes(u1.getID(), 0);

        assertTrue(accessibleNodes.containsKey(oa1.getID()));
        assertTrue(accessibleNodes.containsKey(o1.getID()));
        assertTrue(accessibleNodes.containsKey(o2.getID()));
        assertTrue(accessibleNodes.containsKey(o3.getID()));

        assertEquals(new OperationSet("read", "write"), accessibleNodes.get(oa1.getID()));
        assertEquals(new OperationSet("read", "write"), accessibleNodes.get(o1.getID()));
        assertEquals(new OperationSet("read", "write"), accessibleNodes.get(o2.getID()));
        assertEquals(new OperationSet("read", "write"), accessibleNodes.get(o3.getID()));
    }

    @Test
    void testGraph1() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph2() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID(), pc2.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID(), ua2.getID());

        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph3() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph4() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID(), ua2.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));
        graph.associate(ua2.getID(), oa1.getID(), new OperationSet("write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph5() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc2.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID(), ua2.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));
        graph.associate(ua2.getID(), oa2.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read")));
    }
    @Test
    void testGraph6() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc2.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID(), ua2.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));
        graph.associate(ua2.getID(), oa2.getID(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read")));
    }
    @Test
    void testGraph7() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph8() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("*"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph9() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("*"));
        graph.associate(ua2.getID(), oa1.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph10() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc2.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID(), ua2.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("*"));
        graph.associate(ua2.getID(), oa2.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph11() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("*"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph12() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID(), ua2.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));
        graph.associate(ua2.getID(), oa1.getID(), new OperationSet("write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph13() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, ua2.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, oa2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("*"));
        graph.associate(ua2.getID(), oa2.getID(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph14() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID(), ua2.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID(), pc2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("*"));
        graph.associate(ua2.getID(), oa1.getID(), new OperationSet("*"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph15() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, ua2.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, oa2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("*"));
        graph.associate(ua2.getID(), oa2.getID(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph16() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, ua2.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));
        graph.associate(ua2.getID(), oa1.getID(), new OperationSet("write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }

    // removed graph7 due to adding the parent IDs to the createNode, need to always connect to the graph.

    @Test
    void testGraph18() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph19() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua2.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph20() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID(), ua2.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));
        graph.associate(ua2.getID(), oa2.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read")));
    }
    @Test
    void testGraph21() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID(), ua2.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));
        graph.associate(ua2.getID(), oa2.getID(), new OperationSet("write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph22() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node pc2 = graph.createPolicyClass(getID(), "pc2", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGraph23WithProhibitions() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa3 = graph.createNode(getID(), "oa3", OA, null, pc1.getID());
        Node oa4 = graph.createNode(getID(), "oa4", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, oa3.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, oa4.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa3.getID(), new OperationSet("read", "write", "execute"));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition();
        prohibition.setName("deny");
        prohibition.setSubject(new Prohibition.Subject(ua1.getID(), Prohibition.Subject.Type.USER_ATTRIBUTE));
        prohibition.setOperations(new OperationSet("read"));
        prohibition.addNode(new Prohibition.Node(oa1.getID(), false));
        prohibition.addNode(new Prohibition.Node(oa2.getID(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        prohibition = new Prohibition("deny2", new Prohibition.Subject(u1.getID(), Prohibition.Subject.Type.USER));
        prohibition.setOperations(new OperationSet("write"));
        prohibition.addNode(new Prohibition.Node(oa3.getID(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
        Set<String> list = decider.list(u1.getID(), 0, o1.getID());
        assertEquals(1, list.size());
        assertTrue(list.contains("execute"));
    }

    @Test
    void testGraph24WithProhibitions() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());
        Node o2 = graph.createNode(getID(), "o2", O, null, oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition("deny", new Prohibition.Subject(ua1.getID(), Prohibition.Subject.Type.USER_ATTRIBUTE));
        prohibition.setOperations(new OperationSet("read"));
        prohibition.addNode(new Prohibition.Node(oa1.getID(), false));
        prohibition.addNode(new Prohibition.Node(oa2.getID(), true));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).contains("read"));
        assertTrue(decider.list(u1.getID(), 0, o2.getID()).isEmpty());

        graph.associate(ua1.getID(), oa2.getID(), new OperationSet("read"));

        prohibition = new Prohibition("deny-process", new Prohibition.Subject(1234, Prohibition.Subject.Type.PROCESS));
        prohibition.setOperations(new OperationSet("read"));
        prohibition.addNode(new Prohibition.Node(oa1.getID(), false));
        prohibitions.add(prohibition);

        assertTrue(decider.list(u1.getID(), 1234, o1.getID()).isEmpty());
    }

    @Test
    void testGraph25WithProhibitions() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, oa1.getID());
        Node oa3 = graph.createNode(getID(), "oa3", OA, null, oa1.getID());
        Node oa4 = graph.createNode(getID(), "oa4", OA, null, oa3.getID());
        Node oa5 = graph.createNode(getID(), "oa5", OA, null, oa2.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa4.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition("deny", new Prohibition.Subject(u1.getID(), Prohibition.Subject.Type.USER));
        prohibition.setOperations(new OperationSet("read", "write"));
        prohibition.addNode(new Prohibition.Node(oa4.getID(), true));
        prohibition.addNode(new Prohibition.Node(oa1.getID(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
        assertTrue(decider.list(u1.getID(), 0, oa5.getID()).isEmpty());
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGraph25WithProhibitions2() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, pc1.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition("deny", new Prohibition.Subject(u1.getID(), Prohibition.Subject.Type.USER));
        prohibition.setOperations(new OperationSet("read", "write"));
        prohibition.addNode(new Prohibition.Node(oa1.getID(), false));
        prohibition.addNode(new Prohibition.Node(oa2.getID(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }

    @Test
    void testDeciderWithUA() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass(getID(), "pc1", null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null, pc1.getID());
        Node ua1 = graph.createNode(getID(), "ua1", UA, null, ua2.getID());
        Node u1 = graph.createNode(getID(), "u1", U, null, ua1.getID());
        Node oa1 = graph.createNode(getID(), "oa1", OA, null, pc1.getID());
        Node oa2 = graph.createNode(getID(), "oa2", OA, null, pc1.getID());
        Node o1 = graph.createNode(getID(), "o1", O, null, oa1.getID(), oa2.getID());
        Node o2 = graph.createNode(getID(), "o2", O, null, oa2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read"));
        graph.associate(ua2.getID(), oa1.getID(), new OperationSet("write"));

        Decider decider = new PReviewDecider(graph);
        Set<String> permissions = decider.list(ua1.getID(), 0, oa1.getID());
        assertTrue(permissions.containsAll(Arrays.asList("read", "write")));
    }
}