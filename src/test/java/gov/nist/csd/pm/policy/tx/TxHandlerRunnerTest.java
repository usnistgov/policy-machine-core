package gov.nist.csd.pm.policy.tx;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.NodeNameExistsException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.tx.TxRunner.runTx;
import static org.junit.jupiter.api.Assertions.*;

class TxHandlerRunnerTest {

    @Test
    void testRunTx() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        runTx(pap, () -> {
            pap.createPolicyClass("pc1");
        });

        assertTrue(pap.nodeExists("pc1"));

        assertThrows(NodeNameExistsException.class, () -> runTx(pap, () -> {
            pap.deleteNode("pc1");
            pap.createPolicyClass("pc2");
            // expect error and rollback
            pap.createPolicyClass("pc2");
        }));

        assertTrue(pap.nodeExists("pc1"));
        assertFalse(pap.nodeExists("pc2"));
    }

}