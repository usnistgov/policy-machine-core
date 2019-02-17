package gov.nist.csd.pm.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.loader.Neo4jGraphLoader;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.graph.model.nodes.NodeUtils;
import gov.nist.csd.pm.graph.search.MemGraphSearch;
import gov.nist.csd.pm.graph.search.Neo4jSearch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.OA;
import static gov.nist.csd.pm.graph.model.nodes.NodeType.PC;
import static gov.nist.csd.pm.graph.model.nodes.NodeType.UA;
import static org.junit.jupiter.api.Assertions.*;


class MemGraphIT {

    private static Graph loadedGraph;
    private static String testID;
    private static long pc1ID;
    private static long oa1ID;
    private static long ua1ID;
    private static long o1ID;
    private static long u1ID;

    @BeforeEach
    void setUp() throws PMException, IOException {
        // set up neo4j graph
        Neo4jGraph neoGraph = new Neo4jGraph(TestUtils.getDatabaseContext());
        testID = UUID.randomUUID().toString();

        u1ID = neoGraph.createNode(new NodeContext(5, "u1", NodeType.U, NodeUtils.toProperties("namespace", testID)));
        o1ID = neoGraph.createNode(new NodeContext(3, "o1", NodeType.O, NodeUtils.toProperties("namespace", testID)));

        ua1ID = neoGraph.createNode(new NodeContext(4, "ua1", NodeType.UA, NodeUtils.toProperties("namespace", testID)));
        neoGraph.assign(new NodeContext(u1ID, NodeType.U), new NodeContext(ua1ID, NodeType.UA));

        oa1ID = neoGraph.createNode(new NodeContext(2, "oa1", NodeType.OA, NodeUtils.toProperties("namespace", testID)));
        neoGraph.assign(new NodeContext(o1ID, NodeType.O), new NodeContext(oa1ID, NodeType.OA));

        pc1ID = neoGraph.createNode(new NodeContext(1, "pc1", PC, NodeUtils.toProperties("namespace", testID)));
        neoGraph.assign(new NodeContext(ua1ID, NodeType.UA), new NodeContext(pc1ID, PC));
        neoGraph.assign(new NodeContext(oa1ID, NodeType.OA), new NodeContext(pc1ID, PC));

        neoGraph.associate(new NodeContext(ua1ID, NodeType.UA), new NodeContext(oa1ID, NodeType.OA), new HashSet<>(Arrays
                .asList("read", "write")));

        loadedGraph = new MemGraph(new Neo4jGraphLoader(TestUtils.getDatabaseContext()));
    }

    @AfterEach
    void tearDown() throws PMException, IOException {
        Neo4jGraph neoGraph = new Neo4jGraph(TestUtils.getDatabaseContext());
        HashSet<NodeContext> nodes = new Neo4jSearch(TestUtils.getDatabaseContext()).search(null, null, NodeUtils.toProperties("namespace", testID));
        for(NodeContext node : nodes) {
            neoGraph.deleteNode(node.getID());
        }
    }

    @Test
    public void testCreateNode() throws PMException {
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
        long nodeID = graph.createNode(new NodeContext(1234, "oa", OA, NodeUtils.toProperties("namespace", "test")));

        // check node is added
        MemGraphSearch search = new MemGraphSearch(graph);
        NodeContext node = search.getNode(nodeID);
        assertEquals("oa", node.getName());
        assertEquals(OA, node.getType());
    }

    @Test
    public void testUpdateNode() throws PMException {
        MemGraph graph = new MemGraph();
        NodeContext node = new NodeContext(123, "node", OA, NodeUtils.toProperties("namespace", "test"));
        long nodeID = graph.createNode(node);

        // node not found
        assertThrows(PMException.class, () -> graph.updateNode(new NodeContext(9, "newNodeName", null, null)));

        // update name
        graph.updateNode(node.name("updated name"));
        assertEquals(graph.getNodesMap().get(nodeID).getName(), "updated name");

        // update properties
        graph.updateNode(node.property("newKey", "newValue"));
        assertEquals(graph.getNodesMap().get(nodeID).getProperties().get("newKey"), "newValue");
    }

    @Test
    public void testDeleteNode() throws PMException {
        MemGraph graph = new MemGraph();
        long id = graph.createNode(new NodeContext(123, "node", PC, NodeUtils.toProperties("namespace", "test")));

        graph.deleteNode(id);

        // deleted from the graph
        assertFalse(graph.exists(id));

        // deleted from the node map
        assertFalse(graph.getNodesMap().containsKey(id));

        // deleted from list of policies
        assertFalse(graph.getPolicies().contains(id));
    }

    @Test
    public void testExists() throws PMException {
        Graph graph = new MemGraph();
        long id = graph.createNode(new NodeContext(123, "node", OA, null));
        assertTrue(graph.exists(id));
        assertFalse(graph.exists(1234));

        // test the loaded graph
        assertTrue(loadedGraph.exists(pc1ID));
        assertTrue(loadedGraph.exists(oa1ID));
        assertTrue(loadedGraph.exists(ua1ID));
        assertTrue(loadedGraph.exists(u1ID));
        assertTrue(loadedGraph.exists(o1ID));
    }

    @Test
    public void testGetPolicies() throws PMException {
        Graph graph = new MemGraph();

        assertTrue(graph.getPolicies().isEmpty());

        graph.createNode(new NodeContext(123, "node1", PC, null));
        graph.createNode(new NodeContext(1234, "node2", PC, null));
        graph.createNode(new NodeContext(1235, "node3", PC, null));

        assertEquals(3, graph.getPolicies().size());

        // loaded graph
        assertTrue(loadedGraph.getPolicies().contains(pc1ID));
    }

    @Test
    public void testGetChildren() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        long parentID = graph.createNode(new NodeContext(1, "parent", OA, null));
        long child1ID = graph.createNode(new NodeContext(2, "child1", OA, null));
        long child2ID = graph.createNode(new NodeContext(3, "child2", OA, null));

        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parentID, OA));
        graph.assign(new NodeContext(child2ID, OA), new NodeContext(parentID, OA));

        HashSet<NodeContext> children = graph.getChildren(parentID);
        assertTrue(children.containsAll(Arrays.asList(
                new NodeContext().id(child1ID),
                new NodeContext().id(child2ID)
        )));

        // loaded graph
        children = loadedGraph.getChildren(pc1ID);
        assertTrue(children.containsAll(Arrays.asList(
                new NodeContext().id(oa1ID),
                new NodeContext().id(ua1ID)
        )));

        children = loadedGraph.getChildren(oa1ID);
        assertTrue(children.contains(new NodeContext().id(o1ID)));

        children = loadedGraph.getChildren(ua1ID);
        assertTrue(children.contains(new NodeContext().id(u1ID)));
    }

    @Test
    public void testGetParents() throws PMException {
        Graph graph = new MemGraph();

        assertThrows(PMException.class, () -> graph.getChildren(1));

        long parent1ID = graph.createNode(new NodeContext(1, "parent1", OA, null));
        long parent2ID = graph.createNode(new NodeContext(2, "parent2", OA, null));
        long child1ID = graph.createNode(new NodeContext(3, "child1", OA, null));

        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parent1ID, OA));
        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parent2ID, OA));

        HashSet<NodeContext> parents = graph.getParents(child1ID);
        assertTrue(parents.contains(new NodeContext().id(parent1ID)));
        assertTrue(parents.contains(new NodeContext().id(parent2ID)));

        // loaded graph
        parents = loadedGraph.getParents(oa1ID);
        assertTrue(parents.contains(new NodeContext().id(pc1ID)));
        parents = loadedGraph.getParents(ua1ID);
        assertTrue(parents.contains(new NodeContext().id(pc1ID)));
        parents = loadedGraph.getParents(o1ID);
        assertTrue(parents.contains(new NodeContext().id(oa1ID)));
        parents = loadedGraph.getParents(u1ID);
        assertTrue(parents.contains(new NodeContext().id(ua1ID)));
    }

    @Test
    public void testAssign() throws PMException {
        Graph graph = new MemGraph();

        long parent1ID = graph.createNode(new NodeContext(1, "parent1", OA, null));
        long child1ID = graph.createNode(new NodeContext(3, "child1", OA, null));

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign(null, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new NodeContext(), null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new NodeContext().id(123), null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new NodeContext().id(1), new NodeContext().id(123)))
        );

        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parent1ID, OA));

        assertTrue(graph.getChildren(parent1ID).contains(new NodeContext().id(child1ID)));
        assertTrue(graph.getParents(child1ID).contains(new NodeContext().id(parent1ID)));
    }

    @Test
    public void testDeassign() throws PMException {
        Graph graph = new MemGraph();

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign(null, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(new NodeContext(), null))
        );

        long parent1ID = graph.createNode(new NodeContext(1, "parent1", OA, null));
        long child1ID = graph.createNode(new NodeContext(3, "child1", OA, null));

        graph.assign(new NodeContext(child1ID, OA), new NodeContext(parent1ID, OA));
        graph.deassign(new NodeContext(child1ID, OA), new NodeContext(parent1ID, OA));

        assertFalse(graph.getChildren(parent1ID).contains(new NodeContext().id(child1ID)));
        assertFalse(graph.getParents(child1ID).contains(new NodeContext().id(parent1ID)));
    }

    @Test
    public void testAssociate() throws PMException {
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
    public void testDissociate() throws PMException {
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
    public void testGetSourceAssociations() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new NodeContext(1, "ua", UA, null));
        long targetID = graph.createNode(new NodeContext(3, "target", OA, null));

        graph.associate(new NodeContext(uaID, UA), new NodeContext(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));

        HashMap<Long, HashSet<String>> associations = graph.getSourceAssociations(uaID);
        assertTrue(associations.containsKey(targetID));
        assertTrue(associations.get(targetID).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getSourceAssociations(123));

        // loaded graph
        associations = loadedGraph.getSourceAssociations(ua1ID);
        assertTrue(associations.containsKey(oa1ID));
        assertTrue(associations.get(oa1ID).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    public void testGetTargetAssociations() throws PMException {
        Graph graph = new MemGraph();

        long uaID = graph.createNode(new NodeContext(1, "ua", UA, null));
        long targetID = graph.createNode(new NodeContext(3, "target", OA, null));

        graph.associate(new NodeContext(uaID, UA), new NodeContext(targetID, OA), new HashSet<>(Arrays.asList("read", "write")));

        HashMap<Long, HashSet<String>> associations = graph.getTargetAssociations(targetID);
        assertTrue(associations.containsKey(uaID));
        assertTrue(associations.get(uaID).containsAll(Arrays.asList("read", "write")));

        assertThrows(PMException.class, () -> graph.getTargetAssociations(123));

        // loaded graph
        associations = loadedGraph.getTargetAssociations(oa1ID);
        assertTrue(associations.containsKey(ua1ID));
        assertTrue(associations.get(ua1ID).containsAll(Arrays.asList("read", "write")));
    }
}