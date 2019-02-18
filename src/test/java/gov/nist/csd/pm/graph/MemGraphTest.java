package gov.nist.csd.pm.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;


class MemGraphTest {

    @Test
    void testCreateNode() throws PMException {
        MemGraph graph = new MemGraph();
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(new NodeContext())),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(new NodeContext(123, null, OA, null))),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(new NodeContext(123, "", OA, null))),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(new NodeContext(123, "name", null, null)))
        );

        // add pc
        long pc = graph.createNode(new NodeContext(123, "pc", PC, null));
        assertTrue(graph.getPolicies().contains(pc));

        // add non pc
        long nodeID = graph.createNode(new NodeContext(1234, "oa", OA, NodeContext.toProperties("namespace", "test")));

        // check node is added
        NodeContext node = graph.getNode(nodeID);
        assertEquals("oa", node.getName());
        assertEquals(OA, node.getType());
    }

    @Test
    void testUpdateNode() throws PMException {
        MemGraph graph = new MemGraph();
        NodeContext node = new NodeContext(123, "node", OA, NodeContext.toProperties("namespace", "test"));
        long nodeID = graph.createNode(node);

        // node not found
        assertThrows(PMException.class, () -> graph.updateNode(new NodeContext(9, "newNodeName", null, null)));

        // update name
        graph.updateNode(node.name("updated name"));
        assertEquals(graph.getNode(nodeID).getName(), "updated name");

        // update properties
        graph.updateNode(node.property("newKey", "newValue"));
        assertEquals(graph.getNode(nodeID).getProperties().get("newKey"), "newValue");
    }

    @Test
    void testDeleteNode() throws PMException {
        MemGraph graph = new MemGraph();
        long id = graph.createNode(new NodeContext(123, "node", PC, NodeContext.toProperties("namespace", "test")));

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
        long id = graph.createNode(new NodeContext(123, "node", OA, null));
        assertTrue(graph.exists(id));
        assertFalse(graph.exists(1234));
    }

    @Test
    void testGetPolicies() throws PMException {
        Graph graph = new MemGraph();

        assertTrue(graph.getPolicies().isEmpty());

        graph.createNode(new NodeContext(123, "node1", PC, null));
        graph.createNode(new NodeContext(1234, "node2", PC, null));
        graph.createNode(new NodeContext(1235, "node3", PC, null));

        assertEquals(3, graph.getPolicies().size());
    }

    @Test
    void testGetChildren() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        long parentID = graph.createNode(new NodeContext(1, "parent", OA, null));
        long child1ID = graph.createNode(new NodeContext(2, "child1", OA, null));
        long child2ID = graph.createNode(new NodeContext(3, "child2", OA, null));

        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parentID, OA));
        graph.assign(new NodeContext(child2ID, OA), new NodeContext(parentID, OA));

        HashSet<Long> children = graph.getChildren(parentID);
        assertTrue(children.containsAll(Arrays.asList(child1ID, child2ID)));
    }

    @Test
    void testGetParents() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        long parent1ID = graph.createNode(new NodeContext(1, "parent1", OA, null));
        long parent2ID = graph.createNode(new NodeContext(2, "parent2", OA, null));
        long child1ID = graph.createNode(new NodeContext(3, "child1", OA, null));

        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parent1ID, OA));
        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parent2ID, OA));

        HashSet<Long> parents = graph.getParents(child1ID);
        assertTrue(parents.contains(parent1ID));
        assertTrue(parents.contains(parent2ID));
    }

    @Test
    void testAssign() throws PMException {
        Graph graph = new MemGraph();

        long parent1ID = graph.createNode(new NodeContext(1, "parent1", OA, null));
        long child1ID = graph.createNode(new NodeContext(3, "child1", OA, null));

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign(null, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new NodeContext(), null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new NodeContext().id(123), null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new NodeContext().id(1), new NodeContext().id(123)))
        );

        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parent1ID, OA));

        assertTrue(graph.getChildren(parent1ID).contains(child1ID));
        assertTrue(graph.getParents(child1ID).contains(parent1ID));
    }

    @Test
    void testDeassign() throws PMException {
        Graph graph = new MemGraph();

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign(null, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new NodeContext(), null))
        );

        long parent1ID = graph.createNode(new NodeContext(1, "parent1", OA, null));
        long child1ID = graph.createNode(new NodeContext(3, "child1", OA, null));

        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parent1ID, OA));
        graph.deassign(new NodeContext(child1ID, OA), new NodeContext(parent1ID, OA));

        assertFalse(graph.getChildren(parent1ID).contains(child1ID));
        assertFalse(graph.getParents(child1ID).contains(parent1ID));
    }

    @Test
    void testAssociate() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new NodeContext(1, "ua", UA, null));
        long targetID = graph.createNode(new NodeContext(3, "target", OA, null));

        graph.associate(new NodeContext(uaID, UA), new NodeContext(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));

        HashMap<Long, HashSet<String>> associations = graph.getSourceAssociations(uaID);
        assertTrue(associations.containsKey(targetID));
        assertTrue(associations.get(targetID).containsAll(Arrays.asList("read", "write")));

        associations = graph.getTargetAssociations(targetID);
        assertTrue(associations.containsKey(uaID));
        assertTrue(associations.get(uaID).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testDissociate() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new NodeContext(1, "ua", UA, null));
        long targetID = graph.createNode(new NodeContext(3, "target", OA, null));

        graph.associate(new NodeContext(uaID, UA), new NodeContext(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));
        graph.dissociate(new NodeContext(uaID, UA), new NodeContext(targetID, OA));

        HashMap<Long, HashSet<String>> associations = graph.getSourceAssociations(uaID);
        assertFalse(associations.containsKey(targetID));

        associations = graph.getTargetAssociations(targetID);
        assertFalse(associations.containsKey(uaID));
    }

    @Test
    void testGetSourceAssociations() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new NodeContext(1, "ua", UA, null));
        long targetID = graph.createNode(new NodeContext(3, "target", OA, null));

        graph.associate(new NodeContext(uaID, UA), new NodeContext(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));

        HashMap<Long, HashSet<String>> associations = graph.getSourceAssociations(uaID);
        assertTrue(associations.containsKey(targetID));
        assertTrue(associations.get(targetID).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getSourceAssociations(123));
    }

    @Test
    void testGetTargetAssociations() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new NodeContext(1, "ua", UA, null));
        long targetID = graph.createNode(new NodeContext(3, "target", OA, null));

        graph.associate(new NodeContext(uaID, UA), new NodeContext(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));

        HashMap<Long, HashSet<String>> associations = graph.getTargetAssociations(targetID);
        assertTrue(associations.containsKey(uaID));
        assertTrue(associations.get(uaID).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getTargetAssociations(123));
    }

    @Test
    void testSearch() throws PMException {
        Graph graph = new MemGraph();

        graph.createNode(new NodeContext(1, "oa1", OA, NodeContext.toProperties("namespace", "test")));
        graph.createNode(new NodeContext(2, "oa2", OA, NodeContext.toProperties("key1", "value1")));
        graph.createNode(new NodeContext(3, "oa3", OA, NodeContext.toProperties("key1", "value1", "key2", "value2")));

        // name and type no properties
        HashSet<NodeContext> nodes = graph.search("oa1", OA.toString(), null);
        assertEquals(1, nodes.size());

        // one property
        nodes = graph.search(null, null, NodeContext.toProperties("key1", "value1"));
        assertEquals(2, nodes.size());

        // just namespace
        nodes = graph.search(null, null, NodeContext.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());

        // name, type, namespace
        nodes = graph.search("oa1", OA.toString(), NodeContext.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());

        nodes = graph.search(null, OA.toString(), NodeContext.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());
        nodes = graph.search(null, OA.toString(), null);
        assertEquals(3, nodes.size());
        nodes = graph.search(null, OA.toString(), NodeContext.toProperties("key1", "value1"));
        assertEquals(2, nodes.size());
        nodes = graph.search(null, null, null);
        assertEquals(3, nodes.size());
    }

    @Test
    public void testGetNodes() throws PMException {
        MemGraph graph = new MemGraph();

        assertTrue(graph.getNodes().isEmpty());

        graph.createNode(new NodeContext(123, "node1", OA, null));
        graph.createNode(new NodeContext(1234, "node2", OA, null));
        graph.createNode(new NodeContext(1235, "node3", OA, null));

        assertEquals(3, graph.getNodes().size());
    }

    @Test
    void testGetNode() throws PMException {
        MemGraph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getNode(123));

        long id = graph.createNode(new NodeContext(123, "oa1", OA, null));
        NodeContext node = graph.getNode(id);
        assertEquals("oa1", node.getName());
        assertEquals(OA, node.getType());
    }
}