package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;
import static org.junit.jupiter.api.Assertions.*;

class GraphServiceTest {

    @Test
    void testPolicyClassReps() throws PMException {
        PDP pdp = new PDP(new PAP(new MemGraph(), new MemProhibitions(), new MemObligations()), null, new OperationSet("read", "write", "execute"));
        Graph graph = pdp.getGraphService(new UserContext("super", ""));

        Node test = graph.createPolicyClass("test", null);
        String defUA = test.getProperties().get("default_ua");
        String defOA = test.getProperties().get("default_oa");
        String repProp = test.getProperties().get(REP_PROPERTY);

        assertTrue(graph.exists(defUA));
        assertTrue(graph.exists(defOA));
        assertTrue(graph.exists(repProp));
    }
}