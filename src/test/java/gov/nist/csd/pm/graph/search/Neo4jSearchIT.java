package gov.nist.csd.pm.graph.search;


import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Neo4jGraph;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Neo4jSearchIT {

    private Neo4jGraph  graph;
    private Neo4jSearch search;
    private String      testID;

    @BeforeEach
    public void setUp() throws PMException, IOException {
        graph = new Neo4jGraph(TestUtils.getDatabaseContext());
        search = new Neo4jSearch(TestUtils.getDatabaseContext());
        testID = UUID.randomUUID().toString();
    }

    @AfterEach
    public void tearDown() throws PMException {
        HashSet<NodeContext> nodes = search.search(null, null, NodeUtils.toProperties("namespace", testID));
        for(NodeContext node : nodes) {
            graph.deleteNode(node.getID());
        }
    }

    @Test
    public void testSearch() throws PMException {
        graph.createNode(new NodeContext("oa1", OA, NodeUtils.toProperties("namespace", testID)));
        graph.createNode(new NodeContext("oa2", OA, NodeUtils.toProperties("namespace", testID, "key1", "value1")));
        graph.createNode(new NodeContext("oa3", OA, NodeUtils.toProperties("namespace", testID, "key1", "value1", "key2", "value2")));

        // name and type no properties
        HashSet<NodeContext> nodes = search.search("oa1", OA.toString(), NodeUtils.toProperties("namespace", testID));
        assertEquals(1, nodes.size());

        // one property
        nodes = search.search(null, null, NodeUtils.toProperties("key1", "value1"));
        assertEquals(2, nodes.size());

        // shared property
        nodes = search.search(null, null, NodeUtils.toProperties("namespace", testID));
        assertEquals(3, nodes.size());
    }

    @Test
    public void testGetNodes() throws PMException {
        long node1 = graph.createNode(new NodeContext("node1", OA, NodeUtils.toProperties("namespace", testID)));
        long node2 = graph.createNode(new NodeContext("node2", OA, NodeUtils.toProperties("namespace", testID)));
        long node3 = graph.createNode(new NodeContext("node3", OA, NodeUtils.toProperties("namespace", testID)));

        assertTrue(search.getNodes().containsAll(Arrays.asList(new NodeContext().id(node1), new NodeContext().id(node2), new NodeContext().id(node3))));
    }

    @Test
    public void testGetNode() throws PMException {
        assertThrows(PMException.class, () -> search.getNode(123));

        long id = graph.createNode(new NodeContext("oa1", OA, NodeUtils.toProperties("namespace", testID)));
        NodeContext node = search.getNode(id);
        assertEquals("oa1", node.getName());
        assertEquals(OA, node.getType());
        assertEquals(NodeUtils.toProperties("namespace", testID), node.getProperties());
    }
}