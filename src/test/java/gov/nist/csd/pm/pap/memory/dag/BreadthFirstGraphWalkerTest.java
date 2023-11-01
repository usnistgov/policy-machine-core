package gov.nist.csd.pm.pap.memory.dag;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.dag.walker.bfs.BreadthFirstGraphWalker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BreadthFirstGraphWalkerTest {

    static PAP pap;

    @BeforeAll
    static void setup() throws PMException {
        pap = new PAP(new MemoryPolicyStore());
        pap.graph().createPolicyClass("pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");

        pap.graph().createObjectAttribute("oa1-1", "oa1");
        pap.graph().createObjectAttribute("oa1-1-1", "oa1-1");
        pap.graph().createObjectAttribute("oa1-1-2", "oa1-1");
        pap.graph().createObjectAttribute("oa1-1-3", "oa1-1");

        pap.graph().createObjectAttribute("oa1-2", "oa1");
        pap.graph().createObjectAttribute("oa1-2-1", "oa1-2");
        pap.graph().createObjectAttribute("oa1-2-2", "oa1-2");
        pap.graph().createObjectAttribute("oa1-2-3", "oa1-2");
    }

    @Test
    void testWalk() throws PMException {
        List<String> visited = new ArrayList<>();
        BreadthFirstGraphWalker bfs = new BreadthFirstGraphWalker(pap.graph())
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
        BreadthFirstGraphWalker bfs = new BreadthFirstGraphWalker(pap.graph())
                .withDirection(Direction.CHILDREN)
                .withVisitor(node -> {
                    visited.add(node);
                })
                .withAllPathShortCircuit(node -> node.equals("oa1-2"));

        bfs.walk("pc1");
        List<String> expected = List.of("pc1", "oa1", "oa1-1", "oa1-2");
        assertEquals(expected, visited);

        visited.clear();
        bfs = new BreadthFirstGraphWalker(pap.graph())
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
        BreadthFirstGraphWalker bfs = new BreadthFirstGraphWalker(pap.graph())
                .withDirection(Direction.CHILDREN)
                .withVisitor(visited::add)
                .withSinglePathShortCircuit(node -> node.equals("oa1-1-1"));

        bfs.walk("pc1");
        List<String> expected = List.of("pc1", "oa1", "oa1-1", "oa1-2", "oa1-1-1",
                "oa1-2-1", "oa1-2-2", "oa1-2-3");
        assertEquals(expected, visited);
    }
}