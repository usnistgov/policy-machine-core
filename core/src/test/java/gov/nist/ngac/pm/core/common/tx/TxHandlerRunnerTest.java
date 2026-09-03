/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.common.tx;

import static gov.nist.ngac.pm.core.common.tx.TxRunner.runTx;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.NodeNameExistsException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

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

    @Test
    void testRunTxRollsBackOnRuntimeException() throws PMException {
        PAP pap = new TestPAP();

        runTx(pap, () -> {
            pap.modify().graph().createPolicyClass("pc1");
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("pc1"));

        PMException e = assertThrows(PMException.class, () -> runTx(pap, () -> {
            pap.modify().graph().createPolicyClass("pc2");
            throw new IllegalStateException("test");
        }));
        assertInstanceOf(IllegalStateException.class, e.getCause());
        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertFalse(pap.query().graph().nodeExists("pc2"));

        // Proves MemoryTx counter/active were reset by rollback: pre-fix, the stale
        // active flag / nonzero counter would corrupt this subsequent transaction.
        runTx(pap, () -> {
            pap.modify().graph().createPolicyClass("pc3");
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("pc3"));
    }

}