package gov.nist.csd.pm.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.O;
import static gov.nist.csd.pm.graph.model.nodes.NodeType.OA;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphSerializerTest {

    private static Graph graph;
    private static long u1ID;
    private static long o1ID;
    private static long ua1ID;
    private static long oa1ID;
    private static long pc1ID;

    @BeforeAll
    static void setUp() throws PMException {
        graph = new MemGraph();

        u1ID = graph.createNode(new Node(5, "u1", NodeType.U, null));
        o1ID = graph.createNode(new Node(4, "o1", O, null));
        ua1ID = graph.createNode(new Node(3, "ua1", NodeType.UA, null));
        oa1ID = graph.createNode(new Node(2, "oa1", OA, null));
        pc1ID = graph.createNode(new Node(1, "pc1", NodeType.PC, null));

        graph.assign(new Node(u1ID, NodeType.U), new Node(ua1ID, NodeType.UA));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(ua1ID, NodeType.UA), new Node(pc1ID, NodeType.PC));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, NodeType.PC));

        graph.associate(new Node(ua1ID, NodeType.UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));
    }

    @Test
    void testSerialize() throws PMException {
        String json = GraphSerializer.toJson(graph);
        Graph deGraph = GraphSerializer.fromJson(new MemGraph(), json);

        assertTrue(deGraph.getNodes().containsAll(Arrays.asList(
                new Node().id(u1ID),
                new Node().id(o1ID),
                new Node().id(ua1ID),
                new Node().id(oa1ID),
                new Node().id(pc1ID)
        )));

        assertTrue(deGraph.getChildren(pc1ID).containsAll(Arrays.asList(ua1ID, oa1ID)));
        assertTrue(deGraph.getChildren(oa1ID).contains(o1ID));
        assertTrue(deGraph.getChildren(ua1ID).contains(u1ID));

        assertTrue(deGraph.getSourceAssociations(ua1ID).containsKey(oa1ID));
        assertTrue(deGraph.getSourceAssociations(ua1ID).get(oa1ID).containsAll(Arrays.asList("read", "write")));
    }
}