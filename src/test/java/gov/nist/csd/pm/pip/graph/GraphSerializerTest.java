package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

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

        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, new OperationSet("read", "write"));
    }

    @Test
    void testJson() throws PMException {
        graph = new MemGraph();

        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, new OperationSet("read", "write"));

        String json = GraphSerializer.toJson(graph);
        Graph graph = new MemGraph();
        GraphSerializer.fromJson(graph, json);

        assertTrue(graph.getNodes().containsAll(Arrays.asList(
                new Node("u1", U),
                new Node("o1", O),
                new Node("ua1", UA),
                new Node("oa1", OA),
                new Node("pc1", PC)
        )));

        assertTrue(graph.getChildren(pc1ID).containsAll(Arrays.asList(ua1ID, oa1ID)));
        assertTrue(graph.getChildren(oa1ID).contains(o1ID));
        assertTrue(graph.getChildren(ua1ID).contains(u1ID));

        assertTrue(graph.getSourceAssociations(ua1ID).containsKey(oa1ID));
        assertTrue(graph.getSourceAssociations(ua1ID).get(oa1ID).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testSerialize() throws PMException {
        /*String str =
                "node PC pc1" +
                "node OA oa1" +
                "node UA ua1" +
                "node U u1" +
                "node O o1" +

                "assign U:u1 UA:ua1" +
                "assign O:o1 OA:oa1" +
                "assign OA:oa1 PC:pc1" +

                "assoc UA:ua1 OA:oa1 [read, write]";*/
        String serialize = GraphSerializer.serialize(graph);
        Graph graph = new MemGraph();
        GraphSerializer.deserialize(graph, serialize);

        Set<Node> search = graph.search("u1", null, null);
        assertFalse(search.isEmpty());
        Node uNode = search.iterator().next();

        search = graph.search("ua1", null, null);
        assertFalse(search.isEmpty());
        Node uaNode = search.iterator().next();

        search = graph.search("o1", null, null);
        assertFalse(search.isEmpty());
        Node oNode = search.iterator().next();

        search = graph.search("oa1", null, null);
        assertFalse(search.isEmpty());
        Node oaNode = search.iterator().next();

        search = graph.search("pc1", null, null);
        assertFalse(search.isEmpty());
        Node pcNode = search.iterator().next();

        assertTrue(graph.getChildren(pcNode.getID()).containsAll(Arrays.asList(uaNode.getID(), oaNode.getID())));
        assertTrue(graph.getChildren(oaNode.getID()).contains(oNode.getID()));
        assertTrue(graph.getChildren(uaNode.getID()).contains(uNode.getID()));

        assertTrue(graph.getSourceAssociations(uaNode.getID()).containsKey(oaNode.getID()));
        assertTrue(graph.getSourceAssociations(uaNode.getID()).get(oaNode.getID()).containsAll(Arrays.asList("read", "write")));
    }
}