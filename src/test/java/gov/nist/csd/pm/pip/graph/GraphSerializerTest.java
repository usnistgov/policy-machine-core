package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.O;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphSerializerTest {

    private static Graph graph;
    private static long u1ID = 1;
    private static long o1ID = 2;
    private static long ua1ID = 3;
    private static long oa1ID = 4;
    private static long pc1ID = 5;

    @BeforeAll
    static void setUp() throws PMException {
        graph = new MemGraph();

        graph.createNode(u1ID, "u1", NodeType.U, null);
        graph.createNode(o1ID, "o1", O, null);
        graph.createNode(ua1ID, "ua1", NodeType.UA, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", NodeType.PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new OperationSet("read", "write"));
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