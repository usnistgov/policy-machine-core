package gov.nist.csd.pm.graph.search;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.MemGraph;
import gov.nist.csd.pm.graph.Neo4jGraph;
import gov.nist.csd.pm.graph.loader.Neo4jGraphLoader;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.graph.model.nodes.NodeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.OA;
import static org.junit.jupiter.api.Assertions.*;

class MemGraphSearchTest {

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

        pc1ID = neoGraph.createNode(new NodeContext(1, "pc1", NodeType.PC, NodeUtils.toProperties("namespace", testID)));
        neoGraph.assign(new NodeContext(ua1ID, NodeType.UA), new NodeContext(pc1ID, NodeType.PC));
        neoGraph.assign(new NodeContext(oa1ID, NodeType.OA), new NodeContext(pc1ID, NodeType.PC));

        neoGraph.associate(new NodeContext(ua1ID, NodeType.UA), new NodeContext(oa1ID, NodeType.OA), new HashSet<>(Arrays.asList("read", "write")));

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
    void testSearch() throws PMException {
        MemGraph graph = new MemGraph();
        Search search = new MemGraphSearch(graph);

        graph.createNode(new NodeContext(1, "oa1", OA, NodeUtils.toProperties("namespace", "test")));
        graph.createNode(new NodeContext(2, "oa2", OA, NodeUtils.toProperties("key1", "value1")));
        graph.createNode(new NodeContext(3, "oa3", OA, NodeUtils.toProperties("key1", "value1", "key2", "value2")));

        // name and type no properties
        HashSet<NodeContext> nodes = search.search("oa1", OA.toString(), null);
        assertEquals(1, nodes.size());

        // one property
        nodes = search.search(null, null, NodeUtils.toProperties("key1", "value1"));
        assertEquals(2, nodes.size());

        // just namespace
        nodes = search.search(null, null, NodeUtils.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());

        // name, type, namespace
        nodes = search.search("oa1", OA.toString(), NodeUtils.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());

        nodes = search.search(null, OA.toString(), NodeUtils.toProperties("namespace", "test"));
        assertEquals(1, nodes.size());
        nodes = search.search(null, OA.toString(), null);
        assertEquals(3, nodes.size());
        nodes = search.search(null, OA.toString(), NodeUtils.toProperties("key1", "value1"));
        assertEquals(2, nodes.size());
        nodes = search.search(null, null, null);
        assertEquals(3, nodes.size());
    }

    @Test
    public void testGetNodes() throws PMException {
        MemGraph graph = new MemGraph();
        Search search = new MemGraphSearch(graph);

        assertTrue(search.getNodes().isEmpty());

        graph.createNode(new NodeContext(123, "node1", OA, null));
        graph.createNode(new NodeContext(1234, "node2", OA, null));
        graph.createNode(new NodeContext(1235, "node3", OA, null));

        assertEquals(3, search.getNodes().size());

        // laoded graph
        search = new MemGraphSearch((MemGraph) loadedGraph);
        HashSet<NodeContext> nodes = search.getNodes();
        assertTrue(nodes.containsAll(Arrays.asList(
                new NodeContext().id(pc1ID),
                new NodeContext().id(oa1ID),
                new NodeContext().id(ua1ID),
                new NodeContext().id(u1ID),
                new NodeContext().id(o1ID)
        )));
    }

    @Test
    void testGetNode() throws PMException {
        MemGraph graph = new MemGraph();
        Search search = new MemGraphSearch(graph);

        assertThrows(PMException.class, () -> search.getNode(123));

        long id = graph.createNode(new NodeContext(123, "oa1", OA, null));
        NodeContext node = search.getNode(id);
        assertEquals("oa1", node.getName());
        assertEquals(OA, node.getType());
    }
}