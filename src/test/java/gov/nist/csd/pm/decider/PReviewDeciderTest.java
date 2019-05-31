package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.O;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PReviewDeciderTest {

    private static MemGraph graph;
    private static final long     u1ID = 1;
    private static final long     o1ID = 2;
    private static final long     o2ID = 3;
    private static final long     o3ID = 4;
    private static final long     oa1ID = 5;

    @BeforeAll
    static void setUp() throws PMException {
        graph = new MemGraph();

        graph.createNode(u1ID, "u1", NodeType.U, null);
        graph.createNode(o1ID, "o1", O, null);
        graph.createNode(o2ID, "o2", O, null);
        graph.createNode(o3ID, "o3", O, null);
        Node ua1 = graph.createNode(6, "ua1", NodeType.UA, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        Node pc1 = graph.createNode(8, "pc1", NodeType.PC, null);

        graph.assign(u1ID, ua1.getID());
        graph.assign(o1ID, oa1ID);
        graph.assign(o2ID, oa1ID);
        graph.assign(o3ID, oa1ID);
        graph.assign(ua1.getID(), pc1.getID());
        graph.assign(oa1ID, pc1.getID());

        graph.associate(ua1.getID(), oa1ID, new HashSet<>(Arrays.asList("read", "write")));
    }

    @Test
    void testHasPermissions() throws PMException {
        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.hasPermissions(u1ID, o1ID, "read", "write"));
    }

    @Test
    void testListPermissions() throws PMException {
        for(TestCases.TestCase tc : TestCases.getTests()) {
            PReviewDecider decider = new PReviewDecider(tc.getGraph());
            Set<String> result = decider.listPermissions(TestCases.u1ID, TestCases.o1ID);

            assertEquals(tc.getExpectedOps(), result, tc.getName());
        }
    }

    @Test
    void testFilter() {
        Collection<Node> nodes = graph.getNodes();
        List<Long> nodeIDs = new ArrayList<>();
        for (Node node : nodes) {
            nodeIDs.add(node.getID());
        }
        PReviewDecider decider = new PReviewDecider(graph);
        assertEquals(new HashSet<>(Arrays.asList(o1ID, o2ID, o3ID, oa1ID)),
                new HashSet<>(decider.filter(u1ID, nodeIDs, "read"))
        );
    }

    @Test
    void testGetChildren() throws PMException {
        PReviewDecider decider = new PReviewDecider(graph);
        Collection<Long> children = decider.getChildren(u1ID, oa1ID);
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