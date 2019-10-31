package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.BeforeAll;
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

        Node u1 = graph.createNode(getID(), "u1", NodeType.U, null);
        Node o1 = graph.createNode(getID(), "o1", O, null);
        Node o2 = graph.createNode(getID(), "o2", O, null);
        Node o3 = graph.createNode(getID(), "o3", O, null);
        Node ua1 = graph.createNode(getID(), "ua1", NodeType.UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", NodeType.PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o2.getID(), oa1.getID());
        graph.assign(o3.getID(), oa1.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.check(u1.getID(), 0, o1.getID(), "read", "write"));
    }

    @Test
    void testFilter() throws PMException {
        Graph graph = new MemGraph();

        Node u1 = graph.createNode(getID(), "u1", NodeType.U, null);
        Node o1 = graph.createNode(getID(), "o1", O, null);
        Node o2 = graph.createNode(getID(), "o2", O, null);
        Node o3 = graph.createNode(getID(), "o3", O, null);
        Node ua1 = graph.createNode(getID(), "ua1", NodeType.UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", NodeType.PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o2.getID(), oa1.getID());
        graph.assign(o3.getID(), oa1.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));
        Collection<Node> nodes = graph.getNodes();
        List<Long> nodeIDs = new ArrayList<>();
        for (Node node : nodes) {
            nodeIDs.add(node.getID());
        }
        PReviewDecider decider = new PReviewDecider(graph);
        assertEquals(new HashSet<>(Arrays.asList(o1.getID(), o2.getID(), o3.getID(), oa1.getID())),
                new HashSet<>(decider.filter(u1.getID(), 0, nodeIDs, "read"))
        );
    }

    @Test
    void testGetChildren() throws PMException {
        Graph graph = new MemGraph();

        Node u1 = graph.createNode(getID(), "u1", NodeType.U, null);
        Node o1 = graph.createNode(getID(), "o1", O, null);
        Node o2 = graph.createNode(getID(), "o2", O, null);
        Node o3 = graph.createNode(getID(), "o3", O, null);
        Node ua1 = graph.createNode(getID(), "ua1", NodeType.UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", NodeType.PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o2.getID(), oa1.getID());
        graph.assign(o3.getID(), oa1.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        Collection<Long> children = decider.getChildren(u1.getID(), 0, oa1.getID());
        assertEquals(
                new HashSet<>(Arrays.asList(o1.getID(), o2.getID(), o3.getID())),
                children
        );
    }

    @Test
    void testGetAccessibleNodes() throws PMException {
        Graph graph = new MemGraph();

        Node u1 = graph.createNode(getID(), "u1", NodeType.U, null);
        Node o1 = graph.createNode(getID(), "o1", O, null);
        Node o2 = graph.createNode(getID(), "o2", O, null);
        Node o3 = graph.createNode(getID(), "o3", O, null);
        Node ua1 = graph.createNode(getID(), "ua1", NodeType.UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", NodeType.PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o2.getID(), oa1.getID());
        graph.assign(o3.getID(), oa1.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        Map<Long, Set<String>> accessibleNodes = decider.getAccessibleNodes(u1.getID(), 0);

        assertTrue(accessibleNodes.containsKey(oa1.getID()));
        assertTrue(accessibleNodes.containsKey(o1.getID()));
        assertTrue(accessibleNodes.containsKey(o2.getID()));
        assertTrue(accessibleNodes.containsKey(o3.getID()));

        assertEquals(new HashSet<>(Arrays.asList("read", "write")), accessibleNodes.get(oa1.getID()));
        assertEquals(new HashSet<>(Arrays.asList("read", "write")), accessibleNodes.get(o1.getID()));
        assertEquals(new HashSet<>(Arrays.asList("read", "write")), accessibleNodes.get(o2.getID()));
        assertEquals(new HashSet<>(Arrays.asList("read", "write")), accessibleNodes.get(o3.getID()));
    }

    @Test
    void testGraph1() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph2() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(u1.getID(), ua2.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(ua1.getID(), pc2.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(oa2.getID(), pc2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph3() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph4() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(u1.getID(), ua2.getID());
        graph.assign(ua2.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2.getID(), oa1.getID(), new HashSet<>(Arrays.asList("write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph5() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(u1.getID(), ua2.getID());
        graph.assign(ua1.getID(), pc2.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(oa2.getID(), pc2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2.getID(), oa2.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read")));
    }
    @Test
    void testGraph6() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(u1.getID(), ua2.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(oa2.getID(), pc2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));
        graph.associate(ua2.getID(), oa2.getID(), new HashSet<>(Arrays.asList("read")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read")));
    }
    @Test
    void testGraph7() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(oa2.getID(), pc2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph8() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("*")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph9() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(ua1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph10() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(u1.getID(), ua2.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(oa2.getID(), pc2.getID());
        graph.assign(ua1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2.getID(), oa2.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph11() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(oa2.getID(), pc2.getID());
        graph.assign(ua1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("*")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph12() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(u1.getID(), ua2.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(ua2.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2.getID(), oa1.getID(), new HashSet<>(Arrays.asList("write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph13() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), ua2.getID());
        graph.assign(ua2.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), oa2.getID());
        graph.assign(oa2.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2.getID(), oa2.getID(), new HashSet<>(Arrays.asList("read")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph14() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(u1.getID(), ua2.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(oa1.getID(), pc2.getID());
        graph.assign(ua1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2.getID(), oa1.getID(), new HashSet<>(Arrays.asList("*")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph15() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), ua2.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), oa2.getID());
        graph.assign(oa2.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2.getID(), oa2.getID(), new HashSet<>(Arrays.asList("read")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("*")));
    }
    @Test
    void testGraph16() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), ua2.getID());
        graph.assign(ua2.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2.getID(), oa1.getID(), new HashSet<>(Arrays.asList("write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph17() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);


        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph18() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph19() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph20() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node o1 = graph.createNode(getID(), "o1", O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(u1.getID(), ua2.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(ua1.getID(), pc2.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(oa2.getID(), pc2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2.getID(), oa2.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read")));
    }
    @Test
    void testGraph21() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(u1.getID(), ua2.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(ua1.getID(), pc2.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(oa2.getID(), pc2.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2.getID(), oa2.getID(), new HashSet<>(Arrays.asList("write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).isEmpty());
    }
    @Test
    void testGraph22() throws PMException {
        Graph graph = new MemGraph();
        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);
        Node pc2 = graph.createNode(getID(), "pc2", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGraph23WithProhibitions() throws PMException {
        Graph graph = new MemGraph();

        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node oa3 = graph.createNode(getID(), "oa3", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(oa2.getID(), oa3.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(oa3.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa3.getID(), new HashSet<>(Arrays.asList("read", "write", "execute")));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition();
        prohibition.setName("deny");
        prohibition.setSubject(new Prohibition.Subject(ua1.getID(), Prohibition.Subject.Type.USER_ATTRIBUTE));
        prohibition.setOperations(new HashSet<>(Arrays.asList("read")));
        prohibition.addNode(new Prohibition.Node(oa1.getID(), false));
        prohibition.addNode(new Prohibition.Node(oa2.getID(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        prohibition = new Prohibition("deny2", new Prohibition.Subject(u1.getID(), Prohibition.Subject.Type.USER));
        prohibition.setOperations(new HashSet<>(Arrays.asList("write")));
        prohibition.addNode(new Prohibition.Node(oa3.getID(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).containsAll(Arrays.asList("execute")));
    }

    @Test
    void testGraph24WithProhibitions() throws PMException {
        Graph graph = new MemGraph();

        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node o1 = graph.createNode(getID(), "o1", NodeType.O, null);
        Node o2 = graph.createNode(getID(), "o2", NodeType.O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(o2.getID(), oa2.getID());
        graph.assign(oa2.getID(), pc1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition("deny", new Prohibition.Subject(ua1.getID(), Prohibition.Subject.Type.USER_ATTRIBUTE));
        prohibition.setOperations(new HashSet<>(Arrays.asList("read")));
        prohibition.addNode(new Prohibition.Node(oa1.getID(), false));
        prohibition.addNode(new Prohibition.Node(oa2.getID(), true));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
        assertTrue(decider.list(u1.getID(), 0, o1.getID()).contains("read"));
        assertTrue(decider.list(u1.getID(), 0, o2.getID()).isEmpty());

        graph.associate(ua1.getID(), oa2.getID(), new HashSet<>(Arrays.asList("read")));

        prohibition = new Prohibition("deny-process", new Prohibition.Subject(1234, Prohibition.Subject.Type.PROCESS));
        prohibition.setOperations(new HashSet<>(Arrays.asList("read")));
        prohibition.addNode(new Prohibition.Node(oa1.getID(), false));
        prohibitions.add(prohibition);

        assertTrue(decider.list(u1.getID(), 1234, o1.getID()).isEmpty());
    }

    @Test
    void testDeciderWithUA() throws PMException {
        Graph graph = new MemGraph();

        Node u1 = graph.createNode(getID(), "u1", U, null);
        Node ua1 = graph.createNode(getID(), "ua1", UA, null);
        Node ua2 = graph.createNode(getID(), "ua2", UA, null);
        Node o1 = graph.createNode(getID(), "o1", O, null);
        Node o2 = graph.createNode(getID(), "o2", O, null);
        Node oa1 = graph.createNode(getID(), "oa1", OA, null);
        Node oa2 = graph.createNode(getID(), "oa2", OA, null);
        Node pc1 = graph.createNode(getID(), "pc1", PC, null);

        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), ua2.getID());
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(o1.getID(), oa2.getID());
        graph.assign(o2.getID(), oa2.getID());
        graph.assign(oa2.getID(), pc1.getID());
        graph.assign(oa1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2.getID(), oa1.getID(), new HashSet<>(Arrays.asList("write")));

        Decider decider = new PReviewDecider(graph);
        Set<String> permissions = decider.list(ua1.getID(), 0, oa1.getID());
        assertTrue(permissions.containsAll(Arrays.asList("read", "write")));
    }
}