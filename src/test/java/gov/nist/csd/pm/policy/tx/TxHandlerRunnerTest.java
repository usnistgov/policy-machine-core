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
            pap.graph().createPolicyClass("pc1");
        });

        assertTrue(pap.graph().nodeExists("pc1"));

        assertThrows(NodeNameExistsException.class, () -> runTx(pap, () -> {
            pap.graph().deleteNode("pc1");
            pap.graph().createPolicyClass("pc2");
            // expect error and rollback
            pap.graph().createPolicyClass("pc2");
        }));

        assertTrue(pap.graph().nodeExists("pc1"));
        assertFalse(pap.graph().nodeExists("pc2"));
    }

}