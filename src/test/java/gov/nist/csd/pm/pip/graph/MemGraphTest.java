package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.memory.MemGraph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;


class MemGraphTest {

    @Test
    void testCreateNode() throws PMException {
        MemGraph graph = new MemGraph();

        // add pc
        Node pc = graph.createPolicyClass("pc", null);
        assertTrue(graph.getPolicyClasses().contains(pc.getName()));
        
        assertAll(() -> assertThrows(PMException.class, () -> graph.createNode(null, null, null, "pc")),
                () -> assertThrows(PMException.class, () -> graph.createNode(null, OA, null, "pc")),
                () -> assertThrows(PMException.class, () -> graph.createNode("name", null, null, "pc"))
        );
        
        // add non pc
        Node node = graph.createNode("oa", OA, Node.toProperties("namespace", "test"), pc.getName());

        // check node is added
        node = graph.getNode(node.getName());
        assertEquals("oa", node.getName());
        assertEquals(OA, node.getType());
    }

    @Test
    void testUpdateNode() throws PMException {
        MemGraph graph = new MemGraph();
        Node node = graph.createPolicyClass("node", Node.toProperties("namespace", "test"));

        // node not found
        assertThrows(PMException.class, () -> graph.updateNode("newNodeName", null));

        // update properties
        graph.updateNode("node", Node.toProperties("newKey", "newValue"));
        assertEquals(graph.getNode(node.getName()).getProperties().get("newKey"), "newValue");
    }

    @Test
    void testDeleteNode() throws PMException {
        MemGraph graph = new MemGraph();
        Node node = graph.createPolicyClass("node", Node.toProperties("namespace", "test"));

        graph.deleteNode(node.getName());

        // deleted from the graph
        assertFalse(graph.exists(node.getName()));

        assertThrows(PMException.class, () -> graph.getNode(node.getName()));

        // deleted from list of policies
        assertFalse(graph.getPolicyClasses().contains(node.getName()));
    }

    @Test
    void testExists() throws PMException {
        Graph graph = new MemGraph();
        Node pc = graph.createPolicyClass("pc", null);
        Node oa = graph.createNode("oa", OA, null, pc.getName());
        assertTrue(graph.exists(oa.getName()));
        assertFalse(graph.exists("1234"));
    }

    @Test
    void testGetPolicies() throws PMException {
        Graph graph = new MemGraph();

        assertTrue(graph.getPolicyClasses().isEmpty());

        graph.createPolicyClass("node1", null);
        graph.createPolicyClass("node2", null);
        graph.createPolicyClass("node3", null);

        assertEquals(3, graph.getPolicyClasses().size());
    }

    @Test
    void testGetChildren() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren("l"));

        Node parentNode = graph.createPolicyClass("parent", null);
        Node child1Node = graph.createNode("child1", OA, null, "parent");
        Node child2Node = graph.createNode("child2", OA, null, "parent");

        Set<String> children = graph.getChildren(parentNode.getName());
        assertTrue(children.containsAll(Arrays.asList(child1Node.getName(), child2Node.getName())));
    }

    @Test
    void testGetParents() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren("l"));

        Node parent1Node = graph.createPolicyClass("parent1", null);
        Node parent2Node = graph.createNode("parent2", OA, null, "parent1");
        Node child1Node = graph.createNode("child1", OA, null, "parent1", "parent2");

        Set<String> parents = graph.getParents(child1Node.getName());
        assertTrue(parents.contains(parent1Node.getName()));
        assertTrue(parents.contains(parent2Node.getName()));
    }

    @Test
    void testAssign() throws PMException {
        Graph graph = new MemGraph();

        Node parent1Node = graph.createPolicyClass("parent1", null);
        Node child1Node = graph.createNode("child1", OA, null, "parent1");
        Node child2Node = graph.createNode("child2", OA, null, "parent1");

        assertAll(() -> assertThrows(PMException.class, () -> graph.assign("1241124", "123442141")),
                () -> assertThrows(PMException.class, () -> graph.assign("1", "12341234"))
        );

        graph.assign(child1Node.getName(), child2Node.getName());

        assertTrue(graph.getChildren(parent1Node.getName()).contains(child1Node.getName()));
        assertTrue(graph.getParents(child1Node.getName()).contains(parent1Node.getName()));
    }

    @Test
    void testDeassign() throws PMException {
        Graph graph = new MemGraph();

        Node parent1Node = graph.createPolicyClass("parent1", null);
        Node child1Node = graph.createNode("child1", OA, null, "parent1");

        assertThrows(PMException.class, () -> graph.assign("", ""));
        assertThrows(PMException.class, () -> graph.assign(child1Node.getName(), ""));

        graph.deassign(child1Node.getName(), parent1Node.getName());

        assertFalse(graph.getChildren(parent1Node.getName()).contains(child1Node.getName()));
        assertFalse(graph.getParents(child1Node.getName()).contains(parent1Node.getName()));
    }

    @Test
    void testAssociate() throws PMException {
        Graph graph = new MemGraph();

        Node pcNode = graph.createPolicyClass("pc", null);
        Node uaNode = graph.createNode("subject", UA, null, "pc");
        Node targetNode = graph.createNode("target", OA, null, "pc");

        graph.associate(uaNode.getName(), targetNode.getName(), new OperationSet("read", "write"));

        Map<String, OperationSet> associations = graph.getSourceAssociations(uaNode.getName());
        assertTrue(associations.containsKey(targetNode.getName()));
        assertTrue(associations.get(targetNode.getName()).containsAll(Arrays.asList("read", "write")));

        associations = graph.getTargetAssociations(targetNode.getName());
        assertTrue(associations.containsKey(uaNode.getName()));
        assertTrue(associations.get(uaNode.getName()).containsAll(Arrays.asList("read", "write")));

        graph.createNode("test", UA, null, "subject");
        graph.associate("test", "subject", new OperationSet("read"));
        associations = graph.getSourceAssociations("test");
        assertTrue(associations.containsKey("subject"));
        assertTrue(associations.get("subject").contains("read"));
    }

    @Test
    void testDissociate() throws PMException {
        Graph graph = new MemGraph();

        Node pcNode = graph.createPolicyClass("pc", null);
        Node uaNode = graph.createNode("subject", UA, null, "pc");
        Node targetNode = graph.createNode("target", OA, null, "pc");

        graph.associate(uaNode.getName(), targetNode.getName(), new OperationSet("read", "write"));
        graph.dissociate(uaNode.getName(), targetNode.getName());

        Map<String, OperationSet> associations = graph.getSourceAssociations(uaNode.getName());
        assertFalse(associations.containsKey(targetNode.getName()));

        associations = graph.getTargetAssociations(targetNode.getName());
        assertFalse(associations.containsKey(uaNode.getName()));
    }

    @Test
    void testGetSourceAssociations() throws PMException {
        Graph graph = new MemGraph();

        Node pcNode = graph.createPolicyClass("pc", null);
        Node uaNode = graph.createNode("subject", UA, null, "pc");
        Node targetNode = graph.createNode("target", OA, null, "pc");

        graph.associate(uaNode.getName(), targetNode.getName(), new OperationSet("read", "write"));

        Map<String, OperationSet> associations = graph.getSourceAssociations(uaNode.getName());
        assertTrue(associations.containsKey(targetNode.getName()));
        assertTrue(associations.get(targetNode.getName()).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getSourceAssociations("123"));
    }

    @Test
    void testGetTargetAssociations() throws PMException {
        Graph graph = new MemGraph();

        Node pcNode = graph.createPolicyClass("pc", null);
        Node uaNode = graph.createNode("subject", UA, null, "pc");
        Node targetNode = graph.createNode("target", OA, null, "pc");

        graph.associate(uaNode.getName(), targetNode.getName(), new OperationSet("read", "write"));

        Map<String, OperationSet> associations = graph.getTargetAssociations(targetNode.getName());
        assertTrue(associations.containsKey(uaNode.getName()));
        assertTrue(associations.get(uaNode.getName()).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getTargetAssociations("123"));
    }

    @Test
    void testSearch() throws PMException {
        Graph graph = new MemGraph();

        graph.createPolicyClass("pc", null);
        graph.createNode("oa1", OA, Node.toProperties("namespace", "test"), "pc");
        graph.createNode("oa2", OA, Node.toProperties("key1", "value1"), "pc");
        graph.createNode("oa3", OA, Node.toProperties("key1", "value1", "key2", "value2"), "pc");

        Set<Node> nodes = graph.search(OA, null);
        assertEquals(3, nodes.size());

        // one property
        nodes = graph.search(null, Node.toProperties("key1", "value1"));
        assertEquals(2, nodes.size());

        // just namespace
        nodes = graph.search(null, Node.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());

        nodes = graph.search(OA, Node.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());
        nodes = graph.search(OA, null);
        assertEquals(3, nodes.size());
        nodes = graph.search(OA, Node.toProperties("key1", "value1"));
        assertEquals(2, nodes.size());
        nodes = graph.search(null, null);
        assertEquals(4, nodes.size());
    }

    @Test
    void testGetNodes() throws PMException {
        MemGraph graph = new MemGraph();

        assertTrue(graph.getNodes().isEmpty());

        graph.createPolicyClass("pc", null);
        graph.createNode("node1", OA, null, "pc");
        graph.createNode("node2", OA, null, "pc");
        graph.createNode("node3", OA, null, "pc");

        assertEquals(4, graph.getNodes().size());
    }

    @Test
    void testGetNode() throws PMException {
        MemGraph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getNode("123"));

        Node node = graph.createPolicyClass("pc", null);
        node = graph.getNode(node.getName());
        assertEquals("pc", node.getName());
        assertEquals(PC, node.getType());
    }

    @Test
    void testParallelEdges() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "ua1");
        graph.createNode("u1", U, null, "ua2");
        graph.associate("ua2", "ua1", new OperationSet("r"));
        graph.dissociate("ua2", "ua1");

        assertTrue(graph.isAssigned("ua2", "ua1"));
        assertFalse(graph.getSourceAssociations("ua2").containsKey("ua1"));

        graph.associate("ua2", "ua1", new OperationSet("r"));
        graph.deassign("ua2", "ua1");

        assertFalse(graph.isAssigned("ua2", "ua1"));
        assertTrue(graph.getSourceAssociations("ua2").containsKey("ua1"));
    }

    @Test
    void testGetPolicyClassesUpdatesNodes() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "ua1");
        graph.createNode("u1", U, null, "ua2");
        graph.associate("ua2", "ua1", new OperationSet("r"));
        graph.dissociate("ua2", "ua1");

        Set<String> pcs = graph.getPolicyClasses();
        pcs.add("test");

        pcs = graph.getPolicyClasses();
        assertFalse(pcs.contains("test"));
    }

    @Test
    void testGetNodesUpdatesNodes() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "ua1");
        graph.createNode("u1", U, null, "ua2");
        graph.associate("ua2", "ua1", new OperationSet("r"));
        graph.dissociate("ua2", "ua1");

        Set<Node> nodes = graph.getNodes();
        Node node = nodes.iterator().next();
        assertTrue(node.getProperties().isEmpty());

        node.setProperties(Node.toProperties("a", "b"));
        node = graph.getNode(node.getName());
        assertTrue(node.getProperties().isEmpty());
    }
}