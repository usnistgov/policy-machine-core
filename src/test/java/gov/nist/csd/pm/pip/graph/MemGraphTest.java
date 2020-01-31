package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
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
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(0, null, null, null, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, null, OA, null, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, "", OA, null, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, "name", null, null, 0))
        );

        // add pc
        Node pc = graph.createPolicyClass(123, "pc", null);
        assertTrue(graph.getPolicyClasses().contains(pc.getID()));

        // add non pc
        Node node = graph.createNode(1234, "oa", OA, Node.toProperties("namespace", "test"), pc.getID());

        // check node is added
        node = graph.getNode(node.getID());
        assertEquals("oa", node.getName());
        assertEquals(OA, node.getType());
    }

    @Test
    void testUpdateNode() throws PMException {
        MemGraph graph = new MemGraph();
        Node node = graph.createPolicyClass(123, "node", Node.toProperties("namespace", "test"));

        // node not found
        assertThrows(PMException.class, () -> graph.updateNode(9, "newNodeName", null));

        // update name
        graph.updateNode(node.getID(), "updated name", null);
        assertEquals(graph.getNode(node.getID()).getName(), "updated name");

        // update properties
        graph.updateNode(node.getID(), null, Node.toProperties("newKey", "newValue"));
        assertEquals(graph.getNode(node.getID()).getProperties().get("newKey"), "newValue");
    }

    @Test
    void testDeleteNode() throws PMException {
        MemGraph graph = new MemGraph();
        Node node = graph.createPolicyClass(123, "node", Node.toProperties("namespace", "test"));

        graph.deleteNode(node.getID());

        // deleted from the graph
        assertFalse(graph.exists(node.getID()));

        assertThrows(PMException.class, () -> graph.getNode(node.getID()));

        // deleted from list of policies
        assertFalse(graph.getPolicyClasses().contains(node.getID()));
    }

    @Test
    void testExists() throws PMException {
        Graph graph = new MemGraph();
        Node pc = graph.createPolicyClass(123, "pc", null);
        Node oa = graph.createNode(321, "oa", OA, null, pc.getID());
        assertTrue(graph.exists(oa.getID()));
        assertFalse(graph.exists(1234));
    }

    @Test
    void testGetPolicies() throws PMException {
        Graph graph = new MemGraph();

        assertTrue(graph.getPolicyClasses().isEmpty());

        graph.createPolicyClass(123, "node1", null);
        graph.createPolicyClass(1234, "node2", null);
        graph.createPolicyClass(1235, "node3", null);

        assertEquals(3, graph.getPolicyClasses().size());
    }

    @Test
    void testGetChildren() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        Node parentNode = graph.createPolicyClass(1, "parent", null);
        Node child1Node = graph.createNode(2, "child1", OA, null, 1);
        Node child2Node = graph.createNode(3, "child2", OA, null, 1);

        Set<Long> children = graph.getChildren(parentNode.getID());
        assertTrue(children.containsAll(Arrays.asList(child1Node.getID(), child2Node.getID())));
    }

    @Test
    void testGetParents() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        Node parent1Node = graph.createPolicyClass(1, "parent1", null);
        Node parent2Node = graph.createNode(2, "parent2", OA, null, 1);
        Node child1Node = graph.createNode(3, "child1", OA, null, 1, 2);

        Set<Long> parents = graph.getParents(child1Node.getID());
        assertTrue(parents.contains(parent1Node.getID()));
        assertTrue(parents.contains(parent2Node.getID()));
    }

    @Test
    void testAssign() throws PMException {
        Graph graph = new MemGraph();

        Node parent1Node = graph.createPolicyClass(1, "parent1", null);
        Node child1Node = graph.createNode(3, "child1", OA, null, 1);
        Node child2Node = graph.createNode(4, "child2", OA, null, 1);

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign(1241124, 123442141)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(1, 12341234))
        );

        graph.assign(child1Node.getID(), child2Node.getID());

        assertTrue(graph.getChildren(parent1Node.getID()).contains(child1Node.getID()));
        assertTrue(graph.getParents(child1Node.getID()).contains(parent1Node.getID()));
    }

    @Test
    void testDeassign() throws PMException {
        Graph graph = new MemGraph();

        Node parent1Node = graph.createPolicyClass(1, "parent1", null);
        Node child1Node = graph.createNode(3, "child1", OA, null, 1);

        assertThrows(IllegalArgumentException.class, () -> graph.assign(0, 0));
        assertThrows(IllegalArgumentException.class, () -> graph.assign(child1Node.getID(), 0));

        graph.deassign(child1Node.getID(), parent1Node.getID());

        assertFalse(graph.getChildren(parent1Node.getID()).contains(child1Node.getID()));
        assertFalse(graph.getParents(child1Node.getID()).contains(parent1Node.getID()));
    }

    @Test
    void testAssociate() throws PMException {
        Graph graph = new MemGraph();

        Node pcNode = graph.createPolicyClass(1, "pc", null);
        Node uaNode = graph.createNode(3, "subject", UA, null, 1);
        Node targetNode = graph.createNode(4, "target", OA, null, 1);

        graph.associate(uaNode.getID(), targetNode.getID(), new OperationSet("read", "write"));

        Map<Long, OperationSet> associations = graph.getSourceAssociations(uaNode.getID());
        assertTrue(associations.containsKey(targetNode.getID()));
        assertTrue(associations.get(targetNode.getID()).containsAll(Arrays.asList("read", "write")));

        associations = graph.getTargetAssociations(targetNode.getID());
        assertTrue(associations.containsKey(uaNode.getID()));
        assertTrue(associations.get(uaNode.getID()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testDissociate() throws PMException {
        Graph graph = new MemGraph();

        Node pcNode = graph.createPolicyClass(1, "pc", null);
        Node uaNode = graph.createNode(3, "subject", UA, null, 1);
        Node targetNode = graph.createNode(4, "target", OA, null, 1);

        graph.associate(uaNode.getID(), targetNode.getID(), new OperationSet("read", "write"));
        graph.dissociate(uaNode.getID(), targetNode.getID());

        Map<Long, OperationSet> associations = graph.getSourceAssociations(uaNode.getID());
        assertFalse(associations.containsKey(targetNode.getID()));

        associations = graph.getTargetAssociations(targetNode.getID());
        assertFalse(associations.containsKey(uaNode.getID()));
    }

    @Test
    void testGetSourceAssociations() throws PMException {
        Graph graph = new MemGraph();

        Node pcNode = graph.createPolicyClass(1, "pc", null);
        Node uaNode = graph.createNode(3, "subject", UA, null, 1);
        Node targetNode = graph.createNode(4, "target", OA, null, 1);

        graph.associate(uaNode.getID(), targetNode.getID(), new OperationSet("read", "write"));

        Map<Long, OperationSet> associations = graph.getSourceAssociations(uaNode.getID());
        assertTrue(associations.containsKey(targetNode.getID()));
        assertTrue(associations.get(targetNode.getID()).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getSourceAssociations(123));
    }

    @Test
    void testGetTargetAssociations() throws PMException {
        Graph graph = new MemGraph();

        Node pcNode = graph.createPolicyClass(1, "pc", null);
        Node uaNode = graph.createNode(3, "subject", UA, null, 1);
        Node targetNode = graph.createNode(4, "target", OA, null, 1);

        graph.associate(uaNode.getID(), targetNode.getID(), new OperationSet("read", "write"));

        Map<Long, OperationSet> associations = graph.getTargetAssociations(targetNode.getID());
        assertTrue(associations.containsKey(uaNode.getID()));
        assertTrue(associations.get(uaNode.getID()).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getTargetAssociations(123));
    }

    @Test
    void testSearch() throws PMException {
        Graph graph = new MemGraph();

        graph.createPolicyClass(4, "pc", null);
        graph.createNode(1, "oa1", OA, Node.toProperties("namespace", "test"), 4);
        graph.createNode(2, "oa2", OA, Node.toProperties("key1", "value1"), 4);
        graph.createNode(3, "oa3", OA, Node.toProperties("key1", "value1", "key2", "value2"), 4);

        // name and type no properties
        Set<Node> nodes = graph.search("oa1", OA.toString(), null);
        assertEquals(1, nodes.size());

        // one property
        nodes = graph.search(null, null, Node.toProperties("key1", "value1"));
        assertEquals(2, nodes.size());

        // just namespace
        nodes = graph.search(null, null, Node.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());

        // name, type, namespace
        nodes = graph.search("oa1", OA.toString(), Node.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());

        nodes = graph.search(null, OA.toString(), Node.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());
        nodes = graph.search(null, OA.toString(), null);
        assertEquals(3, nodes.size());
        nodes = graph.search(null, OA.toString(), Node.toProperties("key1", "value1"));
        assertEquals(2, nodes.size());
        nodes = graph.search(null, null, null);
        assertEquals(4, nodes.size());
    }

    @Test
    void testGetNodes() throws PMException {
        MemGraph graph = new MemGraph();

        assertTrue(graph.getNodes().isEmpty());

        graph.createPolicyClass(4, "pc", null);
        graph.createNode(123, "node1", OA, null, 4);
        graph.createNode(1234, "node2", OA, null, 4);
        graph.createNode(1235, "node3", OA, null, 4);

        assertEquals(4, graph.getNodes().size());
    }

    @Test
    void testGetNode() throws PMException {
        MemGraph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getNode(123));

        Node node = graph.createPolicyClass(123, "pc", null);
        node = graph.getNode(node.getID());
        assertEquals("pc", node.getName());
        assertEquals(PC, node.getType());
    }
}