package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.*;

class MemoryPAPTest {

    @Test
    void testTx() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.beginTx();
        pap.graph().createPolicyClass("pc1");
        pap.rollback();
        assertFalse(pap.graph().nodeExists("pc1"));

        pap.beginTx();
        pap.graph().createPolicyClass("pc1");
        pap.commit();
        assertTrue(pap.graph().nodeExists("pc1"));
    }
}