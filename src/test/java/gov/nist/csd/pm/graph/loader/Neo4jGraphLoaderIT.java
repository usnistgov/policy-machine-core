package gov.nist.csd.pm.graph.loader;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Neo4jGraph;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.graph.model.nodes.NodeUtils;
import gov.nist.csd.pm.graph.model.relationships.Assignment;
import gov.nist.csd.pm.graph.model.relationships.Association;
import gov.nist.csd.pm.graph.search.Neo4jSearch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class Neo4jGraphLoaderIT {

    private String     testID;
    private Neo4jGraph graph;

    private long pc1ID;
    private long oa1ID;
    private long ua1ID;
    private long o1ID;
    private long u1ID;

    @BeforeEach
    void setUp() throws PMException, IOException {
        graph = new Neo4jGraph(TestUtils.getDatabaseContext());
        testID = UUID.randomUUID().toString();

        u1ID = graph.createNode(new NodeContext(5, "u1", NodeType.U, NodeUtils.toProperties("namespace", testID )));
        o1ID = graph.createNode(new NodeContext(3, "o1", NodeType.O, NodeUtils.toProperties("namespace", testID )));

        ua1ID = graph.createNode(new NodeContext(4, "ua1", NodeType.UA, NodeUtils.toProperties("namespace", testID )));
        graph.assign(new NodeContext(u1ID, NodeType.U), new NodeContext(ua1ID, NodeType.UA));

        oa1ID = graph.createNode(new NodeContext(2, "oa1", NodeType.OA, NodeUtils.toProperties("namespace", testID )));
        graph.assign(new NodeContext(o1ID, NodeType.O), new NodeContext(oa1ID, NodeType.OA));

        pc1ID = graph.createNode(new NodeContext(1, "pc1", NodeType.PC, NodeUtils.toProperties("namespace", testID )));
        graph.assign(new NodeContext(ua1ID, NodeType.UA), new NodeContext(pc1ID, NodeType.PC));
        graph.assign(new NodeContext(oa1ID, NodeType.OA), new NodeContext(pc1ID, NodeType.PC));

        graph.associate(new NodeContext(ua1ID, NodeType.UA), new NodeContext(oa1ID, NodeType.OA), new HashSet<>(Arrays.asList("read", "write")));
    }

    @AfterEach
    void tearDown() throws PMException, IOException {
        HashSet<NodeContext> nodes = new Neo4jSearch(TestUtils.getDatabaseContext()).search(null, null, NodeUtils.toProperties("namespace", testID));
        for(NodeContext node : nodes) {
            graph.deleteNode(node.getID());
        }
    }

    @Test
    void testGetNodes() throws PMException, IOException {
        Neo4jGraphLoader loader = new Neo4jGraphLoader(TestUtils.getDatabaseContext());
        HashSet<NodeContext> nodes = loader.getNodes();
        assertTrue(nodes.contains(new NodeContext().id(pc1ID)));
        assertTrue(nodes.contains(new NodeContext().id(oa1ID)));
        assertTrue(nodes.contains(new NodeContext().id(ua1ID)));
        assertTrue(nodes.contains(new NodeContext().id(u1ID)));
        assertTrue(nodes.contains(new NodeContext().id(o1ID)));
    }

    @Test
    void testGetAssignments() throws PMException, IOException {
        Neo4jGraphLoader loader = new Neo4jGraphLoader(TestUtils.getDatabaseContext());
        HashSet<Assignment> assignments = loader.getAssignments();
        assertTrue(assignments.contains(new Assignment(u1ID, ua1ID)));
        assertTrue(assignments.contains(new Assignment(o1ID, oa1ID)));
        assertTrue(assignments.contains(new Assignment(oa1ID, pc1ID)));
        assertTrue(assignments.contains(new Assignment(ua1ID, pc1ID)));
    }

    @Test
    void testGetAssociations() throws PMException, IOException {
        Neo4jGraphLoader loader = new Neo4jGraphLoader(TestUtils.getDatabaseContext());
        HashSet<Association> associations = loader.getAssociations();
        assertTrue(associations.contains(new Association(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read", "write")))));
    }
}