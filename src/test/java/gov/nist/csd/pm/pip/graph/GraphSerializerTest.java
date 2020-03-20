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

    @BeforeAll
    static void setUp() throws PMException {
        graph = new MemGraph();

        graph.createPolicyClass("pc1", null);
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", new OperationSet("read", "write"));
    }

    @Test
    void testJson() throws PMException {
        graph = new MemGraph();

        graph.createPolicyClass("pc1", null);
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", new OperationSet("read", "write"));

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

        assertTrue(graph.getChildren("pc1").containsAll(Arrays.asList("oa1")));
        assertTrue(graph.getChildren("oa1").contains("o1"));
        assertTrue(graph.getChildren("ua1").contains("u1"));

        assertTrue(graph.getSourceAssociations("ua1").containsKey("oa1"));
        assertTrue(graph.getSourceAssociations("ua1").get("oa1").containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testSerialize() throws PMException {
        String serialize = GraphSerializer.serialize(graph);
        Graph graph = new MemGraph();
        GraphSerializer.deserialize(graph, serialize);

        assertTrue(graph.getChildren("pc1").containsAll(Arrays.asList("ua1", "oa1")));
        assertTrue(graph.getChildren("oa1").contains("o1"));
        assertTrue(graph.getChildren("ua1").contains("u1"));

        assertTrue(graph.getSourceAssociations("ua1").containsKey("oa1"));
        assertTrue(graph.getSourceAssociations("ua1").get("oa1").containsAll(Arrays.asList("read", "write")));
    }
}