package gov.nist.csd.pm.impl.memory.pap.dag;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.common.graph.dag.Direction;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BreadthFirstGraphWalkerTest {

    static PAP pap;
    private static long pc1;
    private static long oa1;
    private static long oa11;
    private static long oa111;
    private static long oa112;
    private static long oa113;
    private static long oa12;
    private static long oa121;
    private static long oa122;
    private static long oa123;

    @BeforeAll
    static void setup() throws PMException {
        pap = new TestPAP();
        pc1 = pap.modify().graph().createPolicyClass("pc1");
        oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));

        oa11 = pap.modify().graph().createObjectAttribute("oa1-1", List.of(oa1));
        oa111 = pap.modify().graph().createObjectAttribute("oa1-1-1", List.of(oa11));
        oa112 = pap.modify().graph().createObjectAttribute("oa1-1-2", List.of(oa11));
        oa113 = pap.modify().graph().createObjectAttribute("oa1-1-3", List.of(oa11));

        oa12 = pap.modify().graph().createObjectAttribute("oa1-2", List.of(oa1));
        oa121 = pap.modify().graph().createObjectAttribute("oa1-2-1", List.of(oa12));
        oa122 = pap.modify().graph().createObjectAttribute("oa1-2-2", List.of(oa12));
        oa123 = pap.modify().graph().createObjectAttribute("oa1-2-3", List.of(oa12));
    }

    @Test
    void testWalk() throws PMException {
        List<Long> visited = new ArrayList<>();
        BreadthFirstGraphWalker bfs = new BreadthFirstGraphWalker(pap.query().graph())
                .withDirection(Direction.ASCENDANTS)
                .withVisitor(visited::add);
        bfs.walk(pc1);
        List<Long> expected = List.of(
                pc1,
                oa1,
                oa11,
                oa12,
                oa111,
                oa112,
                oa113,
                oa121,
                oa122,
                oa123
        );

        assertTrue(expected.containsAll(visited));
        assertTrue(visited.containsAll(expected));
    }

    @Test
    void testAllPathsShortCircuit() throws PMException {
        List<Long> visited = new ArrayList<>();
        BreadthFirstGraphWalker bfs = new BreadthFirstGraphWalker(pap.query().graph())
                .withDirection(Direction.ASCENDANTS)
                .withVisitor(node -> {
                    visited.add(node);
                })
                .withAllPathShortCircuit(node -> node == oa12);

        bfs.walk(pc1);

        assertTrue(visited.containsAll(List.of(pc1, oa1, oa12)));

        visited.clear();
        bfs = new BreadthFirstGraphWalker(pap.query().graph())
                .withDirection(Direction.ASCENDANTS)
                .withVisitor(visited::add)
                .withAllPathShortCircuit(node -> node == oa11);

        bfs.walk(pc1);

        assertTrue(visited.containsAll(List.of(pc1, oa1, oa11)));
        assertFalse(visited.containsAll(List.of(oa111, oa112, oa113, oa121, oa122, oa123)));
    }

    @Test
    void testSinglePathShortCircuit() throws PMException {
        List<Long> visited = new ArrayList<>();
        BreadthFirstGraphWalker bfs = new BreadthFirstGraphWalker(pap.query().graph())
                .withDirection(Direction.ASCENDANTS)
                .withVisitor(visited::add)
                .withSinglePathShortCircuit(node -> node == oa11);

        bfs.walk(pc1);

        assertTrue(visited.containsAll(List.of(pc1, oa1, oa11, oa12, oa121, oa122, oa123)));
        assertFalse(visited.containsAll(List.of(oa111, oa112, oa113)));
    }
}