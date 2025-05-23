package gov.nist.csd.pm.core.common.tx;

import gov.nist.csd.pm.core.common.exception.NodeNameExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.common.tx.TxRunner.runTx;
import static org.junit.jupiter.api.Assertions.*;

class TxHandlerRunnerTest {

    @Test
    void testRunTx() throws PMException {
        PAP pap = new TestPAP();

        runTx(pap, () -> {
            pap.modify().graph().createPolicyClass("pc1");
            return null;
        });

        assertTrue(pap.query().graph().nodeExists("pc1"));

        assertThrows(NodeNameExistsException.class, () -> runTx(pap, () -> {
            pap.modify().graph().deleteNode(1);
            pap.modify().graph().createPolicyClass("pc2");
            // expect error and rollback
            pap.modify().graph().createPolicyClass("pc2");
            return null;
        }));

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
    }

}