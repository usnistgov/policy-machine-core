package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryPAPTest {

    @Test
    void testTx() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
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