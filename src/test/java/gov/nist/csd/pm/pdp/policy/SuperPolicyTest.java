package gov.nist.csd.pm.pdp.policy;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.policies.SuperPolicy;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.MemGraph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SuperPolicyTest {

    @Test
    void testSuperPolicyWithEmptyGraph() throws PMException {
        Graph graph = new MemGraph();
        SuperPolicy superPolicy = new SuperPolicy();
        superPolicy.configure(graph);
        testForSuperPolicy(graph);
    }

    private void testForSuperPolicy(Graph graph) throws PMException {
        assertTrue(graph.exists("super_pc"));
        assertTrue(graph.exists("super_pc_rep"));
        assertTrue(graph.exists("super_ua1"));
        assertTrue(graph.exists("super_ua2"));
        assertTrue(graph.exists("super"));
        assertTrue(graph.exists("super_oa"));

        assertTrue(graph.getParents("super").containsAll(Arrays.asList("super_ua1", "super_ua2")));
        assertTrue(graph.getParents("super_ua1").containsAll(Arrays.asList("super_pc")));
        assertTrue(graph.getParents("super_ua2").containsAll(Arrays.asList("super_pc")));
        assertTrue(graph.getParents("super_oa").containsAll(Arrays.asList("super_pc")));
        assertTrue(graph.getParents("super_pc_rep").containsAll(Arrays.asList("super_oa")));

        assertTrue(graph.getSourceAssociations("super_ua1").containsKey("super_oa"));
        assertTrue(graph.getSourceAssociations("super_ua2").containsKey("super_ua1"));
    }
}
