package gov.nist.csd.pm.pap.memory.dag;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.graph.dag.AllPathsShortCircuit;
import gov.nist.csd.pm.policy.model.graph.dag.SinglePathShortCircuit;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BreadthFirstGraphWalkerTest {

    static Graph graph;

    @BeforeAll
    static void setup() {
        graph = new Graph();
        graph.createPolicyClass("pc1");
        graph.createObjectAttribute("oa1", "pc1");

        graph.createObjectAttribute("oa1-1", "oa1");
        graph.createObjectAttribute("oa1-1-1", "oa1-1");
        graph.createObjectAttribute("oa1-1-2", "oa1-1");
        graph.createObjectAttribute("oa1-1-3", "oa1-1");

        graph.createObjectAttribute("oa1-2", "oa1");
        graph.createObjectAttribute("oa1-2-1", "oa1-2");
        graph.createObjectAttribute("oa1-2-2", "oa1-2");
        graph.createObjectAttribute("oa1-2-3", "oa1-2");
    }

    @Test
    void testWalk() throws PMException {
        List<String> visited = new ArrayList<>();
        BreadthFirstGraphWalker bfs = new BreadthFirstGraphWalker(graph)
                .withDirection(Direction.CHILDREN)
                .withVisitor(node -> {
                    visited.add(node);
                });
        bfs.walk("pc1");
        List<String> expected = List.of(
                "pc1",
                "oa1",
                "oa1-1",
                "oa1-2",
                "oa1-1-1",
                "oa1-1-2",
                "oa1-1-3",
                "oa1-2-1",
                "oa1-2-2",
                "oa1-2-3"
        );

        assertEquals(expected, visited);
    }

    @Test
    void testAllPathsShortCircuit() throws PMException {
        List<String> visited = new ArrayList<>();
        BreadthFirstGraphWalker bfs = new BreadthFirstGraphWalker(graph)
                .withDirection(Direction.CHILDREN)
                .withVisitor(node -> {
                    visited.add(node);
                })
                .withAllPathShortCircuit(node -> node.equals("oa1-2"));

        bfs.walk("pc1");
        List<String> expected = List.of("pc1", "oa1", "oa1-1", "oa1-2");
        assertEquals(expected, visited);

        visited.clear();
        bfs = new BreadthFirstGraphWalker(graph)
                .withDirection(Direction.CHILDREN)
                .withVisitor(visited::add)
                .withAllPathShortCircuit(node -> node.equals("oa1-1-1"));

        bfs.walk("pc1");
        expected = List.of("pc1", "oa1", "oa1-1", "oa1-2", "oa1-1-1");
        assertEquals(expected, visited);
    }

    @Test
    void testSinglePathShortCircuit() throws PMException {
        List<String> visited = new ArrayList<>();
        BreadthFirstGraphWalker bfs = new BreadthFirstGraphWalker(graph)
                .withDirection(Direction.CHILDREN)
                .withVisitor(visited::add)
                .withSinglePathShortCircuit(node -> node.equals("oa1-1-1"));

        bfs.walk("pc1");
        List<String> expected = List.of("pc1", "oa1", "oa1-1", "oa1-2", "oa1-1-1",
                "oa1-2-1", "oa1-2-2", "oa1-2-3");
        assertEquals(expected, visited);
    }
}