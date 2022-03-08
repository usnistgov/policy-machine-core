package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.common.PolicyStore;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.MemPAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.audit.PReviewAuditor;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.memory.MemObligations;
import gov.nist.csd.pm.pip.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pip.memory.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphServiceTest {

    @Test
    void testPolicyClassReps() throws PMException {
        OperationSet ops = new OperationSet("read", "write", "execute");
        PolicyStore policyStore = new MemoryPolicyStore();
        PDP pdp = PDP.newPDP(
                new MemPAP(policyStore),
                null,
                new PReviewDecider(policyStore.getGraph(), policyStore.getProhibitions(), ops),
                new PReviewAuditor(policyStore.getGraph(), ops));
        Graph graph = pdp.withUser(new UserContext("super")).getGraph();

        Node test = graph.createPolicyClass("test", null);
        String defUA = test.getProperties().get("default_ua");
        String defOA = test.getProperties().get("default_oa");
        String repProp = test.getProperties().get(REP_PROPERTY);

        assertTrue(graph.exists(defUA));
        assertTrue(graph.exists(defOA));
        assertTrue(graph.exists(repProp));
    }
}
