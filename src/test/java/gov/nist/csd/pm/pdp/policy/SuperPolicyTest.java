package gov.nist.csd.pm.pdp.policy;

import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.audit.Auditor;
import gov.nist.csd.pm.pdp.audit.PReviewAuditor;
import gov.nist.csd.pm.pdp.audit.model.Explain;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pdp.services.AnalyticsService;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.O;
import static org.junit.jupiter.api.Assertions.*;

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