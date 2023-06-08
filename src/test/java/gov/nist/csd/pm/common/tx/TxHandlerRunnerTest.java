package gov.nist.csd.pm.common.tx;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.exception.NodeNameExistsException;
import gov.nist.csd.pm.common.exception.PMException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static gov.nist.csd.pm.common.tx.TxRunner.runTx;
import static org.junit.jupiter.api.Assertions.*;

class TxHandlerRunnerTest {

    @Test
    void testRunTx() throws PMException {
        MemoryPAP pap = new MemoryPAP();

        runTx(pap, () -> {
            pap.modify().graph().createPolicyClass("pc1");
        });

        assertTrue(pap.query().graph().nodeExists("pc1"));

        assertThrows(NodeNameExistsException.class, () -> runTx(pap, () -> {
            pap.modify().graph().deleteNode("pc1");
            pap.modify().graph().createPolicyClass("pc2");
            // expect error and rollback
            pap.modify().graph().createPolicyClass("pc2");
        }));

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
    }

}