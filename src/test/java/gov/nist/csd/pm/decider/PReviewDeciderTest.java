package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.memory.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.operations.Operations.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

class PReviewDeciderTest {

    private static final OperationSet RWE = new OperationSet("read", "write", "execute");
    
    @Test
    void testHasPermissions() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());
        Node o2 = graph.createNode("o2", O, null, oa1.getName());
        Node o3 = graph.createNode("o3", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write", "unknown-op"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.check(u1.getName(), "", o1.getName(), "read", "write"));
    }

    @Test
    void testFilter() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());
        Node o2 = graph.createNode("o2", O, null, oa1.getName());
        Node o3 = graph.createNode("o3", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));
        Set<Node> nodes = graph.getNodes();

        Set<String> nodeIDs = new HashSet<>(Arrays.asList(o1.getName(), o2.getName(), o3.getName(), oa1.getName()));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertEquals(nodeIDs, new HashSet<>(decider.filter(u1.getName(), "", nodeIDs, "read"))
        );
    }

    @Test
    void testGetChildren() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());
        Node o2 = graph.createNode("o2", O, null, oa1.getName());
        Node o3 = graph.createNode("o3", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        Set<String> children = decider.getChildren(u1.getName(), "", oa1.getName());
        assertEquals(
                new HashSet<>(Arrays.asList(o1.getName(), o2.getName(), o3.getName())),
                children
        );
    }

    @Test
    void testGetAccessibleNodes() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());
        Node o2 = graph.createNode("o2", O, null, oa1.getName());
        Node o3 = graph.createNode("o3", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        Map<String, Set<String>> accessibleNodes = decider.getCapabilityList(u1.getName(), "");

        assertTrue(accessibleNodes.containsKey(oa1.getName()));
        assertTrue(accessibleNodes.containsKey(o1.getName()));
        assertTrue(accessibleNodes.containsKey(o2.getName()));
        assertTrue(accessibleNodes.containsKey(o3.getName()));

        assertEquals(new OperationSet("read", "write"), accessibleNodes.get(oa1.getName()));
        assertEquals(new OperationSet("read", "write"), accessibleNodes.get(o1.getName()));
        assertEquals(new OperationSet("read", "write"), accessibleNodes.get(o2.getName()));
        assertEquals(new OperationSet("read", "write"), accessibleNodes.get(o3.getName()));
    }

    @Test
    void testGraph1() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph2() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName(), pc2.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName(), ua2.getName());

        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).isEmpty());
    }
    @Test
    void testGraph3() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph4() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName(), ua2.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));
        graph.associate(ua2.getName(), oa1.getName(), new OperationSet("write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph5() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc2.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName(), ua2.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));
        graph.associate(ua2.getName(), oa2.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read")));
    }
    @Test
    void testGraph6() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc2.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName(), ua2.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));
        graph.associate(ua2.getName(), oa2.getName(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read")));
    }
    @Test
    void testGraph7() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).isEmpty());
    }
    @Test
    void testGraph8() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("*"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        Set<String> list = decider.list(u1.getName(), "", o1.getName());
        assertTrue(list.containsAll(ADMIN_OPS));
        assertTrue(list.containsAll(RWE));
    }
    @Test
    void testGraph9() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("*"));
        graph.associate(ua2.getName(), oa1.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        Set<String> list = decider.list(u1.getName(), "", o1.getName());
        assertTrue(list.containsAll(ADMIN_OPS));
        assertTrue(list.containsAll(RWE));
    }
    @Test
    void testGraph10() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc2.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName(), ua2.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("*"));
        graph.associate(ua2.getName(), oa2.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph11() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("*"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).isEmpty());
    }
    @Test
    void testGraph12() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName(), ua2.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));
        graph.associate(ua2.getName(), oa1.getName(), new OperationSet("write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read", "write")));
    }
    @Test
    void testGraph13() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node ua1 = graph.createNode("ua1", UA, null, ua2.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, oa2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("*"));
        graph.associate(ua2.getName(), oa2.getName(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        Set<String> list = decider.list(u1.getName(), "", o1.getName());
        assertTrue(list.containsAll(ADMIN_OPS));
        assertTrue(list.contains(READ));
    }
    @Test
    void testGraph14() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName(), ua2.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName(), pc2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("*"));
        graph.associate(ua2.getName(), oa1.getName(), new OperationSet("*"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        Set<String> list = decider.list(u1.getName(), "", o1.getName());
        assertTrue(list.containsAll(ADMIN_OPS));
        assertTrue(list.containsAll(RWE));
    }
    @Test
    void testGraph15() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node ua1 = graph.createNode("ua1", UA, null, ua2.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, oa2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("*"));
        graph.associate(ua2.getName(), oa2.getName(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        Set<String> list = decider.list(u1.getName(), "", o1.getName());
        assertTrue(list.containsAll(ADMIN_OPS));
        assertTrue(list.containsAll(RWE));
    }
    @Test
    void testGraph16() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node ua1 = graph.createNode("ua1", UA, null, ua2.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));
        graph.associate(ua2.getName(), oa1.getName(), new OperationSet("write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read", "write")));
    }

    // removed graph7 due to adding the parent IDs to the createNode, need to always connect to the graph.

    @Test
    void testGraph18() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).isEmpty());
    }
    @Test
    void testGraph19() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua2.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).isEmpty());
    }
    @Test
    void testGraph20() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName(), ua2.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));
        graph.associate(ua2.getName(), oa2.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read")));
    }
    @Test
    void testGraph21() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName(), ua2.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));
        graph.associate(ua2.getName(), oa2.getName(), new OperationSet("write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).isEmpty());
    }
    @Test
    void testGraph22() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node pc2 = graph.createPolicyClass("pc2", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGraph23WithProhibitions() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa3 = graph.createNode("oa3", OA, null, pc1.getName());
        Node oa4 = graph.createNode("oa4", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, oa3.getName());
        Node oa1 = graph.createNode("oa1", OA, null, oa4.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa3.getName(), new OperationSet("read", "write", "execute"));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition.Builder("deny", ua1.getName(), new OperationSet("read"))
                .addContainer(oa1.getName(), false)
                .addContainer(oa2.getName(), false)
                .setIntersection(true)
                .build();
        prohibitions.add(prohibition);

        prohibition = new Prohibition.Builder("deny2", u1.getName(), new OperationSet("write"))
                .setIntersection(true)
                .addContainer(oa3.getName(), false)
                .build();
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions, RWE);
        Set<String> list = decider.list(u1.getName(), "", o1.getName());
        assertEquals(1, list.size());
        assertTrue(list.contains("execute"));
    }

    @Test
    void testGraph24WithProhibitions() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());
        Node o2 = graph.createNode("o2", O, null, oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition.Builder("deny", ua1.getName(), new OperationSet("read"))
                .setIntersection(true)
                .addContainer(oa1.getName(), false)
                .addContainer(oa2.getName(), true)
                .build();
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).contains("read"));
        assertTrue(decider.list(u1.getName(), "", o2.getName()).isEmpty());

        graph.associate(ua1.getName(), oa2.getName(), new OperationSet("read"));

        prohibition = new Prohibition.Builder("deny-process", "1234", new OperationSet("read"))
                .setIntersection(false)
                .addContainer(oa1.getName(), false)
                .build();
        prohibitions.add(prohibition);

        assertTrue(decider.list(u1.getName(), "1234", o1.getName()).isEmpty());
    }

    @Test
    void testGraph25WithProhibitions() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, oa1.getName());
        Node oa3 = graph.createNode("oa3", OA, null, oa1.getName());
        Node oa4 = graph.createNode("oa4", OA, null, oa3.getName());
        Node oa5 = graph.createNode("oa5", OA, null, oa2.getName());
        Node o1 = graph.createNode("o1", O, null, oa4.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition.Builder("deny", u1.getName(), new OperationSet("read", "write"))
                .setIntersection(true)
                .addContainer(oa4.getName(), true)
                .addContainer(oa1.getName(), false)
                .build();
        prohibition.setOperations(new OperationSet("read", "write"));
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions, RWE);
        assertTrue(decider.list(u1.getName(), "", oa5.getName()).isEmpty());
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGraph25WithProhibitions2() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition.Builder("deny", u1.getName(), new OperationSet("read", "write"))
                .setIntersection(true)
                .addContainer(oa1.getName(), false)
                .addContainer(oa2.getName(), false)
                .build();
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions, RWE);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).isEmpty());
    }

    @Test
    void testDeciderWithUA() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua2 = graph.createNode("ua2", UA, null, pc1.getName());
        Node ua1 = graph.createNode("ua1", UA, null, ua2.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName(), oa2.getName());
        Node o2 = graph.createNode("o2", O, null, oa2.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read"));
        graph.associate(ua2.getName(), oa1.getName(), new OperationSet("write"));

        Decider decider = new PReviewDecider(graph, RWE);
        Set<String> permissions = decider.list(ua1.getName(), "", oa1.getName());
        assertTrue(permissions.containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testProhibitionsAllCombinations() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc1");
        graph.createNode("oa3", OA, null, "pc1");
        graph.createNode("oa4", OA, null, "pc1");
        graph.createNode("o1", O, null, "oa1", "oa2", "oa3");
        graph.createNode("o2", O, null, "oa1", "oa4");

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");
        graph.createNode("u2", U, null, "ua1");
        graph.createNode("u3", U, null, "ua1");
        graph.createNode("u4", U, null, "ua1");

        graph.associate("ua1", "oa1", new OperationSet(WRITE, READ));

        Prohibitions prohibitions = new MemProhibitions();
        Prohibition prohibition = new Prohibition.Builder("p1", "u1", new OperationSet(WRITE))
                .setIntersection(true)
                .addContainer("oa1", false)
                .addContainer("oa2", false)
                .addContainer("oa3", false)
                .build();
        prohibitions.add(prohibition);
        prohibition = new Prohibition.Builder("p1", "u2", new OperationSet(WRITE))
                .setIntersection(false)
                .addContainer("oa1", false)
                .addContainer("oa2", false)
                .addContainer("oa3", false)
                .build();
        prohibitions.add(prohibition);
        prohibition = new Prohibition.Builder("p1", "u3", new OperationSet(WRITE))
                .setIntersection(true)
                .addContainer("oa1", false)
                .addContainer("oa2", true)
                .build();
        prohibitions.add(prohibition);
        prohibition = new Prohibition.Builder("p1", "u4", new OperationSet(WRITE))
                .setIntersection(false)
                .addContainer("oa1", false)
                .addContainer("oa2", true)
                .build();
        prohibitions.add(prohibition);
        prohibition = new Prohibition.Builder("p1", "u4", new OperationSet(WRITE))
                .setIntersection(false)
                .addContainer("oa2", true)
                .build();
        prohibitions.add(prohibition);

        Decider decider = new PReviewDecider(graph, prohibitions, RWE);

        Set<String> list = decider.list("u1", "", "o1");
        assertTrue(list.contains(READ) && !list.contains(WRITE));

        list = decider.list("u1", "", "o2");
        assertTrue(list.contains(READ) && list.contains(WRITE));

        list = decider.list("u2", "", "o2");
        assertTrue(list.contains(READ) && !list.contains(WRITE));

        list = decider.list("u3", "", "o2");
        assertTrue(list.contains(READ) && !list.contains(WRITE));

        list = decider.list("u4", "", "o1");
        assertTrue(list.contains(READ) && !list.contains(WRITE));

        list = decider.list("u4", "", "o2");
        assertTrue(list.contains(READ) && !list.contains(WRITE));
    }

    @Test
    void testPermissions() throws PMException {
        Graph graph = new MemGraph();

        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet(ALL_OPS));

        PReviewDecider decider = new PReviewDecider(graph, RWE);
        Set<String> list = decider.list("u1", "", "o1");
        assertTrue(list.containsAll(ADMIN_OPS));
        assertTrue(list.containsAll(RWE));

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet(ALL_ADMIN_OPS));
        list = decider.list("u1", "", "o1");
        assertTrue(list.containsAll(ADMIN_OPS));
        assertFalse(list.containsAll(RWE));

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet(ALL_RESOURCE_OPS));
        list = decider.list("u1", "", "o1");
        assertFalse(list.containsAll(ADMIN_OPS));
        assertTrue(list.containsAll(RWE));
    }

    @Test
    void testPermissionsInOnlyOnePC() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);
        graph.createNode("ua3", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "ua3");
        graph.createNode("u1", UA, null, "ua2");

        graph.createNode("oa1", UA, null, "pc1");
        graph.createNode("oa3", UA, null, "pc2");
        graph.assign("oa3", "oa1");
        graph.createNode("o1", UA, null, "oa3");

        graph.associate("ua3", "oa1", new OperationSet("read"));

        PReviewDecider decider = new PReviewDecider(graph, new OperationSet("read"));
        assertTrue(decider.list("u1", "", "o1").isEmpty());
    }

    private static Graph buildGraph() throws PMException {
        MemGraph graph = new MemGraph();
        Random rand = new Random();
        Node pc1 = graph.createPolicyClass("PC1", null);
        Node pc2 = graph.createPolicyClass("PC2", null);
        Node pc3 = graph.createPolicyClass("PC3", null);

        // first level of UAs
        List<String> uas = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Node ua = null;
            switch (i % 3) {
                case 0:
                    ua = graph.createNode("UA_" + i, UA, null, pc1.getName());
                    break;
                case 1:
                    ua = graph.createNode("UA_" + i, UA, null, pc2.getName());
                    break;
                case 2:
                    ua = graph.createNode("UA_" + i, UA, null, pc3.getName());
                    break;
            }
            if (ua != null) uas.add(ua.getName());

            for (int j = 1; j <= 3; j++) {
                Node childUA = graph.createNode("UA_" + i + "_" + j, UA, null, ua.getName());
                uas.add(childUA.getName());

                for (int k = 1; k <= 2; k++) {
                    Node gcUA = graph.createNode("UA_" + i + "_" + j + "_" + k, UA, null, childUA.getName());
                    uas.add(gcUA.getName());
                }
            }
        }

        // first level of OAs
        List<String> oas = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Node oa = null;
            switch (i % 3) {
                case 0:
                    oa = graph.createNode("OA_" + i, OA, null, pc1.getName());
                    break;
                case 1:
                    oa = graph.createNode("OA_" + i, OA, null, pc1.getName());
                    break;
                case 2:
                    oa = graph.createNode("OA_" + i, OA, null, pc1.getName());
                    break;
            }
            if (oa != null) oas.add(oa.getName());


            for (int j = 1; j <= 6; j++) {
                Node childOA = graph.createNode("OA_" + i + "_" + j, OA, null, oa.getName());
                oas.add(childOA.getName());

                for (int k = 1; k <= 6; k++) {
                    Node gcOA = graph.createNode("OA_" + i + "_" + j + "_" + k, OA, null, childOA.getName());
                    oas.add(gcOA.getName());
                }
            }
        }

        //objects
        for (int i = 1; i <= 400; i++) {
            String iParent = "";
            List<String> parents = new ArrayList<>();
            for (int j = 0; j < 50; j++) {
                String parent = uas.get(rand.nextInt(uas.size()));
                if (parents.contains(parent) || iParent.equals(parent)) {
                    j--;
                    continue;
                }

                if (j == 0) {
                    iParent = parent;
                } else {
                    parents.add(parent);
                }
            }

            Node u = graph.createNode("U_" + i, U, null, iParent, parents.toArray(new String[]{}));
        }

        // associations
        for (String ua : uas) {
            for (int i = 0; i < 5; i ++) {
                String target = oas.get(rand.nextInt(oas.size()));
                graph.associate(ua, target, new OperationSet(READ, WRITE));
            }

            String target = uas.get(rand.nextInt(uas.size()));
            if (target.equals(ua) || graph.getChildren(target).contains(ua) || graph.getParents(ua).contains(target)) {
                continue;
            }

            graph.associate(ua, target, new OperationSet(READ, WRITE));
        }

        return graph;
    }
}