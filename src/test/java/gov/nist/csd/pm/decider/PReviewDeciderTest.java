package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PReviewDeciderTest {
    
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

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("*")));
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

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("*")));
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("*")));
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

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("*")));
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

        PReviewDecider decider = new PReviewDecider(graph);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).containsAll(Arrays.asList("*")));
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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

        PReviewDecider decider = new PReviewDecider(graph);
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
        Prohibition prohibition = new Prohibition();
        prohibition.setName("deny");
        prohibition.setSubject(new Prohibition.Subject(ua1.getName(), Prohibition.Subject.Type.USER_ATTRIBUTE));
        prohibition.setOperations(new OperationSet("read"));
        prohibition.addNode(new Prohibition.Node(oa1.getName(), false));
        prohibition.addNode(new Prohibition.Node(oa2.getName(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        prohibition = new Prohibition("deny2", new Prohibition.Subject(u1.getName(), Prohibition.Subject.Type.USER));
        prohibition.setOperations(new OperationSet("write"));
        prohibition.addNode(new Prohibition.Node(oa3.getName(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
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
        Prohibition prohibition = new Prohibition("deny", new Prohibition.Subject(ua1.getName(), Prohibition.Subject.Type.USER_ATTRIBUTE));
        prohibition.setOperations(new OperationSet("read"));
        prohibition.addNode(new Prohibition.Node(oa1.getName(), false));
        prohibition.addNode(new Prohibition.Node(oa2.getName(), true));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
        assertTrue(decider.list(u1.getName(), "", o1.getName()).contains("read"));
        assertTrue(decider.list(u1.getName(), "", o2.getName()).isEmpty());

        graph.associate(ua1.getName(), oa2.getName(), new OperationSet("read"));

        prohibition = new Prohibition("deny-process", new Prohibition.Subject("1234", Prohibition.Subject.Type.PROCESS));
        prohibition.setOperations(new OperationSet("read"));
        prohibition.addNode(new Prohibition.Node(oa1.getName(), false));
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
        Prohibition prohibition = new Prohibition("deny", new Prohibition.Subject(u1.getName(), Prohibition.Subject.Type.USER));
        prohibition.setOperations(new OperationSet("read", "write"));
        prohibition.addNode(new Prohibition.Node(oa4.getName(), true));
        prohibition.addNode(new Prohibition.Node(oa1.getName(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
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
        Prohibition prohibition = new Prohibition("deny", new Prohibition.Subject(u1.getName(), Prohibition.Subject.Type.USER));
        prohibition.setOperations(new OperationSet("read", "write"));
        prohibition.addNode(new Prohibition.Node(oa1.getName(), false));
        prohibition.addNode(new Prohibition.Node(oa2.getName(), false));
        prohibition.setIntersection(true);
        prohibitions.add(prohibition);

        PReviewDecider decider = new PReviewDecider(graph, prohibitions);
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

        Decider decider = new PReviewDecider(graph);
        Set<String> permissions = decider.list(ua1.getName(), "", oa1.getName());
        assertTrue(permissions.containsAll(Arrays.asList("read", "write")));
    }
}