package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.exceptions.NodeNameExistsException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

class MemoryPDPTest {

    @Test
    void testRollback() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.createPolicyClass("pc1");
        pap.createObjectAttribute("oa1", "pc1");
        pap.createUserAttribute("ua1", "pc1");

        PDP pdp = new MemoryPDP(pap, false);
        assertThrows(NodeNameExistsException.class, () -> {
            pdp.runTx(new UserContext(SUPER_USER), policy -> {
                policy.createPolicyClass("pc2");
                // expect error and rollback
                policy.createObjectAttribute("oa1", "pc2");
            });
        });

        assertTrue(pap.nodeExists("pc1"));
        assertTrue(pap.nodeExists("ua1"));
        assertTrue(pap.nodeExists("oa1"));
        assertFalse(pap.nodeExists("pc2"));
    }
}
