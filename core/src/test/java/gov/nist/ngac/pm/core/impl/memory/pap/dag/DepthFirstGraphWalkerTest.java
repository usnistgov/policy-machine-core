/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.impl.memory.pap.dag;

import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.graph.dag.GraphWalker;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.graph.dag.DepthFirstGraphWalker;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DepthFirstGraphWalkerTest {

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
        GraphWalker bfs = new DepthFirstGraphWalker(pap.query().graph()::getAdjacentAscendants)
                .withVisitor(visited::add);
        bfs.walk(pc1);
        List<Long> expected = List.of(
                oa111, oa112, oa113, oa11, oa121, oa122, oa123, oa12, oa1, pc1
        );

        assertTrue(expected.containsAll(visited));
        assertTrue(visited.containsAll(expected));
    }

    @Test
    void testAllPathsShortCircuit() throws PMException {
        List<Long> visited = new ArrayList<>();
        GraphWalker dfs = new DepthFirstGraphWalker(pap.query().graph()::getAdjacentAscendants)
                .withVisitor(node -> {
                    visited.add(node);
                })
                .withAllPathShortCircuit(nodeId -> nodeId == oa121);

        dfs.walk(pc1);

        List<Long> expected = List.of(oa111, oa112, oa113, oa11, oa121, oa12, oa1, pc1);
        assertTrue(expected.containsAll(visited));
        assertTrue(visited.containsAll(expected));
    }

    @Test
    void testSinglePathShortCircuit() throws PMException {
        List<Long> visited = new ArrayList<>();
        GraphWalker dfs = new DepthFirstGraphWalker(pap.query().graph()::getAdjacentAscendants)
                .withVisitor(visited::add)
                .withSinglePathShortCircuit(nodeId -> nodeId == oa11);

        dfs.walk(pc1);

        List<Long> expected = List.of(oa11, oa121, oa122, oa123, oa12, oa1, pc1);
        assertTrue(expected.containsAll(visited));
        assertTrue(visited.containsAll(expected));
    }
}