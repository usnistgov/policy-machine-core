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
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(new Node())),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(new Node(123, null, OA, null))),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(new Node(123, "", OA, null))),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(new Node(123, "name", null, null)))
        );

        // add pc
        long pc = graph.createNode(new Node(123, "pc", PC, null));
        assertTrue(graph.getPolicies().contains(pc));

        // add non pc
        long nodeID = graph.createNode(new Node(1234, "oa", OA, Node.toProperties("namespace", "test")));

        // check node is added
        Node node = graph.getNode(nodeID);
        assertEquals("oa", node.getName());
        assertEquals(OA, node.getType());
    }

    @Test
    void testUpdateNode() throws PMException {
        MemGraph graph = new MemGraph();
        Node node = new Node(123, "node", OA, Node.toProperties("namespace", "test"));
        long nodeID = graph.createNode(node);

        // node not found
        assertThrows(PMException.class, () -> graph.updateNode(new Node(9, "newNodeName", null, null)));

        // update name
        graph.updateNode(node.name("updated name"));
        assertEquals(graph.getNode(nodeID).getName(), "updated name");

        // update properties
        graph.updateNode(node.property("newKey", "newValue"));
        assertEquals(graph.getNode(nodeID).getProperties().get("newKey"), "newValue");
    }

    @Test
    void testDeleteNode() {
        MemGraph graph = new MemGraph();
        long id = graph.createNode(new Node(123, "node", PC, Node.toProperties("namespace", "test")));

        graph.deleteNode(id);

        // deleted from the graph
        assertFalse(graph.exists(id));

        assertThrows(PMException.class, () -> graph.getNode(id));

        // deleted from list of policies
        assertFalse(graph.getPolicies().contains(id));
    }

    @Test
    void testExists() throws PMException {
        Graph graph = new MemGraph();
        long id = graph.createNode(new Node(123, "node", OA, null));
        assertTrue(graph.exists(id));
        assertFalse(graph.exists(1234));
    }

    @Test
    void testGetPolicies() throws PMException {
        Graph graph = new MemGraph();

        assertTrue(graph.getPolicies().isEmpty());

        graph.createNode(new Node(123, "node1", PC, null));
        graph.createNode(new Node(1234, "node2", PC, null));
        graph.createNode(new Node(1235, "node3", PC, null));

        assertEquals(3, graph.getPolicies().size());
    }

    @Test
    void testGetChildren() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        long parentID = graph.createNode(new Node(1, "parent", OA, null));
        long child1ID = graph.createNode(new Node(2, "child1", OA, null));
        long child2ID = graph.createNode(new Node(3, "child2", OA, null));

        graph.assign(new Node(child1ID, OA), new Node(parentID, OA));
        graph.assign(new Node(child2ID, OA), new Node(parentID, OA));

        Set<Long> children = graph.getChildren(parentID);
        assertTrue(children.containsAll(Arrays.asList(child1ID, child2ID)));
    }

    @Test
    void testGetParents() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        long parent1ID = graph.createNode(new Node(1, "parent1", OA, null));
        long parent2ID = graph.createNode(new Node(2, "parent2", OA, null));
        long child1ID = graph.createNode(new Node(3, "child1", OA, null));

        graph.assign(new Node(child1ID, OA), new Node(parent1ID, OA));
        graph.assign(new Node(child1ID, OA), new Node(parent2ID, OA));

        Set<Long> parents = graph.getParents(child1ID);
        assertTrue(parents.contains(parent1ID));
        assertTrue(parents.contains(parent2ID));
    }

    @Test
    void testAssign() throws PMException {
        Graph graph = new MemGraph();

        long parent1ID = graph.createNode(new Node(1, "parent1", OA, null));
        long child1ID = graph.createNode(new Node(3, "child1", OA, null));

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign(null, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new Node(), null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new Node().id(123), null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new Node().id(1), new Node().id(123)))
        );

        graph.assign(new Node(child1ID, OA), new Node(parent1ID, OA));

        assertTrue(graph.getChildren(parent1ID).contains(child1ID));
        assertTrue(graph.getParents(child1ID).contains(parent1ID));
    }

    @Test
    void testDeassign() throws PMException {
        Graph graph = new MemGraph();

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign(null, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new Node(), null))
        );

        long parent1ID = graph.createNode(new Node(1, "parent1", OA, null));
        long child1ID = graph.createNode(new Node(3, "child1", OA, null));

        graph.assign(new Node(child1ID, OA), new Node(parent1ID, OA));
        graph.deassign(new Node(child1ID, OA), new Node(parent1ID, OA));

        assertFalse(graph.getChildren(parent1ID).contains(child1ID));
        assertFalse(graph.getParents(child1ID).contains(parent1ID));
    }

    @Test
    void testAssociate() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new Node(1, "ua", UA, null));
        long targetID = graph.createNode(new Node(3, "target", OA, null));

        graph.associate(new Node(uaID, UA), new Node(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<Long, Set<String>> associations = graph.getSourceAssociations(uaID);
        assertTrue(associations.containsKey(targetID));
        assertTrue(associations.get(targetID).containsAll(Arrays.asList("read", "write")));

        associations = graph.getTargetAssociations(targetID);
        assertTrue(associations.containsKey(uaID));
        assertTrue(associations.get(uaID).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testDissociate() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new Node(1, "ua", UA, null));
        long targetID = graph.createNode(new Node(3, "target", OA, null));

        graph.associate(new Node(uaID, UA), new Node(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));
        graph.dissociate(new Node(uaID, UA), new Node(targetID, OA));

        Map<Long, Set<String>> associations = graph.getSourceAssociations(uaID);
        assertFalse(associations.containsKey(targetID));

        associations = graph.getTargetAssociations(targetID);
        assertFalse(associations.containsKey(uaID));
    }

    @Test
    void testGetSourceAssociations() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new Node(1, "ua", UA, null));
        long targetID = graph.createNode(new Node(3, "target", OA, null));

        graph.associate(new Node(uaID, UA), new Node(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<Long, Set<String>> associations = graph.getSourceAssociations(uaID);
        assertTrue(associations.containsKey(targetID));
        assertTrue(associations.get(targetID).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getSourceAssociations(123));
    }

    @Test
    void testGetTargetAssociations() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new Node(1, "ua", UA, null));
        long targetID = graph.createNode(new Node(3, "target", OA, null));

        graph.associate(new Node(uaID, UA), new Node(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<Long, Set<String>> associations = graph.getTargetAssociations(targetID);
        assertTrue(associations.containsKey(uaID));
        assertTrue(associations.get(uaID).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getTargetAssociations(123));
    }

    @Test
    void testSearch() throws PMException {
        Graph graph = new MemGraph();

        graph.createNode(new Node(1, "oa1", OA, Node.toProperties("namespace", "test")));
        graph.createNode(new Node(2, "oa2", OA, Node.toProperties("key1", "value1")));
        graph.createNode(new Node(3, "oa3", OA, Node.toProperties("key1", "value1", "key2", "value2")));

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

        graph.createNode(new Node(123, "node1", OA, null));
        graph.createNode(new Node(1234, "node2", OA, null));
        graph.createNode(new Node(1235, "node3", OA, null));

        assertEquals(3, graph.getNodes().size());
    }

    @Test
    void testGetNode() throws PMException {
        MemGraph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getNode(123));

        long id = graph.createNode(new Node(123, "oa1", OA, null));
        Node node = graph.getNode(id);
        assertEquals("oa1", node.getName());
        assertEquals(OA, node.getType());
    }
}