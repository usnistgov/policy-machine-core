package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.MemGraph;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.O;
import static gov.nist.csd.pm.graph.model.nodes.NodeType.OA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PReviewDeciderTest {

    private static MemGraph graph;
    private static long u1ID;
    private static long o1ID;
    private static long o2ID;
    private static long o3ID;
    private static long oa1ID;

    @BeforeAll
    static void setUp() throws PMException {
        graph = new MemGraph();

        u1ID = graph.createNode(new NodeContext(5, "u1", NodeType.U, null));
        o1ID = graph.createNode(new NodeContext(31, "o1", O, null));
        o2ID = graph.createNode(new NodeContext(32, "o2", O, null));
        o3ID = graph.createNode(new NodeContext(33, "o3", O, null));
        long ua1ID = graph.createNode(new NodeContext(4, "ua1", NodeType.UA, null));
        oa1ID = graph.createNode(new NodeContext(2, "oa1", OA, null));
        long pc1ID = graph.createNode(new NodeContext(1, "pc1", NodeType.PC, null));

        graph.assign(new NodeContext(u1ID, NodeType.U), new NodeContext(ua1ID, NodeType.UA));
        graph.assign(new NodeContext(o1ID, O), new NodeContext(oa1ID, OA));
        graph.assign(new NodeContext(o2ID, O), new NodeContext(oa1ID, OA));
        graph.assign(new NodeContext(o3ID, O), new NodeContext(oa1ID, OA));
        graph.assign(new NodeContext(ua1ID, NodeType.UA), new NodeContext(pc1ID, NodeType.PC));
        graph.assign(new NodeContext(oa1ID, OA), new NodeContext(pc1ID, NodeType.PC));

        graph.associate(new NodeContext(ua1ID, NodeType.UA), new NodeContext(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));
    }

    @Test
    void testHasPermissions() throws PMException {
        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.hasPermissions(u1ID, 0, o1ID, "read", "write"));
    }

    @Test
    void testListPermissions() throws PMException {
        PReviewDecider decider = new PReviewDecider(graph);
        assertEquals(new HashSet<>(Arrays.asList("read", "write")), decider.listPermissions(u1ID, 0, o1ID));
    }

    @Test
    void testFilter() {
        Collection<NodeContext> nodes = graph.getNodes();
        List<Long> nodeIDs = new ArrayList<>();
        for(NodeContext node : nodes) {
            nodeIDs.add(node.getID());
        }
        PReviewDecider decider = new PReviewDecider(graph);
        assertEquals(new HashSet<>(Arrays.asList(o1ID, o2ID, o3ID, oa1ID)),
                new HashSet<>(decider.filter(u1ID, 0, nodeIDs, "read"))
        );
    }

    @Test
    void testGetChildren() throws PMException {
        PReviewDecider decider = new PReviewDecider(graph);
        Collection<Long> children = decider.getChildren(u1ID, 0, oa1ID);
        assertEquals(
                new HashSet<>(Arrays.asList(o1ID, o2ID, o3ID)),
                children
        );
    }

    @Test
    void testGetAccessibleNodes() throws PMException {
        PReviewDecider decider = new PReviewDecider(graph);
        Map<Long, Set<String>> accessibleNodes = decider.getAccessibleNodes(u1ID);

        assertTrue(accessibleNodes.containsKey(oa1ID));
        assertTrue(accessibleNodes.containsKey(o1ID));
        assertTrue(accessibleNodes.containsKey(o2ID));
        assertTrue(accessibleNodes.containsKey(o3ID));

        assertEquals(new HashSet<>(Arrays.asList("read", "write")), accessibleNodes.get(oa1ID));
        assertEquals(new HashSet<>(Arrays.asList("read", "write")), accessibleNodes.get(o1ID));
        assertEquals(new HashSet<>(Arrays.asList("read", "write")), accessibleNodes.get(o2ID));
        assertEquals(new HashSet<>(Arrays.asList("read", "write")), accessibleNodes.get(o3ID));
    }
}