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

class GraphSerializerTest {

    private static Graph graph;

    @BeforeAll
    static void setUp() throws PMException {
        graph = new MemGraph();

        long u1ID = graph.createNode(new Node(5, "u1", NodeType.U, null));
        long o1ID = graph.createNode(new Node(31, "o1", O, null));
        long ua1ID = graph.createNode(new Node(4, "ua1", NodeType.UA, null));
        long oa1ID = graph.createNode(new Node(2, "oa1", OA, null));
        long pc1ID = graph.createNode(new Node(1, "pc1", NodeType.PC, null));

        graph.assign(new Node(u1ID, NodeType.U), new Node(ua1ID, NodeType.UA));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(ua1ID, NodeType.UA), new Node(pc1ID, NodeType.PC));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, NodeType.PC));

        graph.associate(new Node(ua1ID, NodeType.UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));
    }

    @Test
    void toJson() throws PMException {
        System.out.println(GraphSerializer.toJson(graph));
    }

    @Test
    void fromJson() {
    }
}