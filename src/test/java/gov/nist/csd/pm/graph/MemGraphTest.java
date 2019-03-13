package gov.nist.csd.pm.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.Node;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;


class MemGraphTest {

    @Test
    void testCreateNode() throws PMException {
        MemGraph graph = new MemGraph();
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(0, null, null, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, null, OA, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, "", OA, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, "name", null, null))
        );

        // add pc
        Node pc = graph.createNode(123, "pc", PC, null);
        assertTrue(graph.getPolicies().contains(pc.getID()));

        // add non pc
        Node node = graph.createNode(1234, "oa", OA, Node.toProperties("namespace", "test"));

        // check node is added
        node = graph.getNode(node.getID());
        assertEquals("oa", node.getName());
        assertEquals(OA, node.getType());
    }

    @Test
    void testUpdateNode() throws PMException {
        MemGraph graph = new MemGraph();
        Node node = graph.createNode( 123, "node", OA, Node.toProperties("namespace", "test"));

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
    void testDeleteNode() {
        MemGraph graph = new MemGraph();
        Node node = graph.createNode(123, "node", PC, Node.toProperties("namespace", "test"));

        graph.deleteNode(node.getID());

        // deleted from the graph
        assertFalse(graph.exists(node.getID()));

        assertThrows(PMException.class, () -> graph.getNode(node.getID()));

        // deleted from list of policies
        assertFalse(graph.getPolicies().contains(node.getID()));
    }

    @Test
    void testExists() throws PMException {
        Graph graph = new MemGraph();
        Node node = graph.createNode(123, "node", OA, null);
        assertTrue(graph.exists(node.getID()));
        assertFalse(graph.exists(1234));
    }

    @Test
    void testGetPolicies() throws PMException {
        Graph graph = new MemGraph();

        assertTrue(graph.getPolicies().isEmpty());

        graph.createNode(123, "node1", PC, null);
        graph.createNode(1234, "node2", PC, null);
        graph.createNode(1235, "node3", PC, null);

        assertEquals(3, graph.getPolicies().size());
    }

    @Test
    void testGetChildren() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        Node parentNode = graph.createNode(1, "parent", OA, null);
        Node child1Node = graph.createNode(2, "child1", OA, null);
        Node child2Node = graph.createNode(3, "child2", OA, null);

        graph.assign(child1Node.getID(), parentNode.getID());
        graph.assign(child2Node.getID(), parentNode.getID());

        Set<Long> children = graph.getChildren(parentNode.getID());
        assertTrue(children.containsAll(Arrays.asList(child1Node.getID(), child2Node.getID())));
    }

    @Test
    void testGetParents() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        Node parent1Node = graph.createNode(1, "parent1", OA, null);
        Node parent2Node = graph.createNode(2, "parent2", OA, null);
        Node child1Node = graph.createNode(3, "child1", OA, null);

        graph.assign(child1Node.getID(), parent1Node.getID());
        graph.assign(child1Node.getID(), parent2Node.getID());

        Set<Long> parents = graph.getParents(child1Node.getID());
        assertTrue(parents.contains(parent1Node.getID()));
        assertTrue(parents.contains(parent2Node.getID()));
    }

    @Test
    void testAssign() throws PMException {
        Graph graph = new MemGraph();

        Node parent1Node = graph.createNode(1, "parent1", OA, null);
        Node child1Node = graph.createNode(3, "child1", OA, null);

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign(1241124, 123442141)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(1, 12341234))
        );

        graph.assign(child1Node.getID(), parent1Node.getID());

        assertTrue(graph.getChildren(parent1Node.getID()).contains(child1Node.getID()));
        assertTrue(graph.getParents(child1Node.getID()).contains(parent1Node.getID()));
    }

    @Test
    void testDeassign() throws PMException {
        Graph graph = new MemGraph();

        Node parent1Node = graph.createNode(1, "parent1", OA, null);
        Node child1Node = graph.createNode(3, "child1", OA, null);

        assertThrows(IllegalArgumentException.class, () -> graph.assign(0, 0));
        assertThrows(IllegalArgumentException.class, () -> graph.assign(child1Node.getID(), 0));

        graph.assign(child1Node.getID(), parent1Node.getID());
        graph.deassign(child1Node.getID(), parent1Node.getID());

        assertFalse(graph.getChildren(parent1Node.getID()).contains(child1Node.getID()));
        assertFalse(graph.getParents(child1Node.getID()).contains(parent1Node.getID()));
    }

    @Test
    void testAssociate() throws PMException {
        Graph graph = new MemGraph();

        Node uaNode = graph.createNode(1, "ua", UA, null);
        Node targetNode = graph.createNode(3, "target", OA, null);

        graph.associate(uaNode.getID(), targetNode.getID(), new HashSet<>(Arrays.asList("read", "write")));

        Map<Long, Set<String>> associations = graph.getSourceAssociations(uaNode.getID());
        assertTrue(associations.containsKey(targetNode.getID()));
        assertTrue(associations.get(targetNode.getID()).containsAll(Arrays.asList("read", "write")));

        associations = graph.getTargetAssociations(targetNode.getID());
        assertTrue(associations.containsKey(uaNode.getID()));
        assertTrue(associations.get(uaNode.getID()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testDissociate() throws PMException {
        Graph graph = new MemGraph();

        Node uaNode = graph.createNode(1, "ua", UA, null);
        Node targetNode = graph.createNode(3, "target", OA, null);

        graph.associate(uaNode.getID(), targetNode.getID(), new HashSet<>(Arrays.asList("read", "write")));
        graph.dissociate(uaNode.getID(), targetNode.getID());

        Map<Long, Set<String>> associations = graph.getSourceAssociations(uaNode.getID());
        assertFalse(associations.containsKey(targetNode.getID()));

        associations = graph.getTargetAssociations(targetNode.getID());
        assertFalse(associations.containsKey(uaNode.getID()));
    }

    @Test
    void testGetSourceAssociations() throws PMException {
        Graph graph = new MemGraph();

        Node uaNode = graph.createNode(1, "ua", UA, null);
        Node targetNode = graph.createNode(3, "target", OA, null);

        graph.associate(uaNode.getID(), targetNode.getID(), new HashSet<>(Arrays.asList("read", "write")));

        Map<Long, Set<String>> associations = graph.getSourceAssociations(uaNode.getID());
        assertTrue(associations.containsKey(targetNode.getID()));
        assertTrue(associations.get(targetNode.getID()).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getSourceAssociations(123));
    }

    @Test
    void testGetTargetAssociations() throws PMException {
        Graph graph = new MemGraph();

        Node uaNode = graph.createNode(1, "ua", UA, null);
        Node targetNode = graph.createNode(3, "target", OA, null);

        graph.associate(uaNode.getID(), targetNode.getID(), new HashSet<>(Arrays.asList("read", "write")));

        Map<Long, Set<String>> associations = graph.getTargetAssociations(targetNode.getID());
        assertTrue(associations.containsKey(uaNode.getID()));
        assertTrue(associations.get(uaNode.getID()).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getTargetAssociations(123));
    }

    @Test
    void testSearch() throws PMException {
        Graph graph = new MemGraph();

        graph.createNode(1, "oa1", OA, Node.toProperties("namespace", "test"));
        graph.createNode(2, "oa2", OA, Node.toProperties("key1", "value1"));
        graph.createNode(3, "oa3", OA, Node.toProperties("key1", "value1", "key2", "value2"));

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
        assertEquals(3, nodes.size());
    }

    @Test
    void testGetNodes() {
        MemGraph graph = new MemGraph();

        assertTrue(graph.getNodes().isEmpty());

        graph.createNode(123, "node1", OA, null);
        graph.createNode(1234, "node2", OA, null);
        graph.createNode(1235, "node3", OA, null);

        assertEquals(3, graph.getNodes().size());
    }

    @Test
    void testGetNode() throws PMException {
        MemGraph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getNode(123));

        Node node = graph.createNode(123, "oa1", OA, null);
        node = graph.getNode(node.getID());
        assertEquals("oa1", node.getName());
        assertEquals(OA, node.getType());
    }
}