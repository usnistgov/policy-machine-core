package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.GraphAdmin;
import gov.nist.csd.pm.pap.ObligationsAdmin;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.ProhibitionsAdmin;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;
import static org.junit.jupiter.api.Assertions.*;

class GraphServiceTest {

    @Test
    void testPolicyClassReps() throws PMException {
        PDP pdp = PDP.newPDP(new PAP(new GraphAdmin(new MemGraph()), new ProhibitionsAdmin(new MemProhibitions()), new ObligationsAdmin(new MemObligations())), null, new OperationSet("read", "write", "execute"));
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