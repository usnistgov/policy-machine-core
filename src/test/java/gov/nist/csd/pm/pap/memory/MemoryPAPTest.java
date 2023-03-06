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
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.beginTx();
        pap.createPolicyClass("pc1");
        pap.rollback();
        assertFalse(pap.nodeExists("pc1"));

        pap.beginTx();
        pap.createPolicyClass("pc1");
        pap.commit();
        assertTrue(pap.nodeExists("pc1"));
    }
}