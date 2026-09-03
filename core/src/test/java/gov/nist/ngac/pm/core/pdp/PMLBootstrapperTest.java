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

package gov.nist.ngac.pm.core.pdp;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.DisconnectedNodeException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.Routine;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pdp.bootstrap.PMLBootstrapper;
import gov.nist.ngac.pm.core.pdp.bootstrap.PolicyBootstrapper;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class PMLBootstrapperTest {

    @Test
    void test() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);

        String input = """
                set resource access rights ["read", "write"]
                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" to "oa1" with ["read"]
                
                assign "u1" to ["ua1"]
                
                op1()
                
                routine1()
                """;

        AdminOperation<?> op1 = new AdminOperation<>("op1", VOID_TYPE, List.of(), List.of()) {

            @Override
            public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
                pap.modify().graph().createPolicyClass("op1");

                return null;
            }

        };

        Routine<?> routine1 = new Routine<>("routine1", VOID_TYPE, List.of()) {
            @Override
            public Void execute(PAP pap, UserContext userCtx, Args actualArgs) throws PMException {
                pap.modify().graph().createPolicyClass("routine1");
                return null;
            }

        };

        pap.javaOperations().register(op1);
        pap.javaOperations().register(routine1);

        // register() alone has no policy effect (so it doesn't trip bootstrap's empty-policy
        // check); createOperation() must run inside the bootstrap tx itself, before the PML
        // that invokes op1()/routine1() executes.
        pdp.bootstrap(new PolicyBootstrapper() {
            @Override
            public void bootstrap(PAP tx) throws PMException {
                tx.modify().operations().createOperation(op1);
                tx.modify().operations().createOperation(routine1);

                new PMLBootstrapper("u1", input).bootstrap(tx);
            }
        });

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("op1"));
        assertTrue(pap.query().graph().nodeExists("routine1"));
        assertTrue(pap.query().graph().nodeExists("ua1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
        assertTrue(pap.query().graph().nodeExists("u1"));
    }

    @Test
    void testBootstrapThrowsExceptionWhenUserNotAssigned() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);

        assertThrows(DisconnectedNodeException.class, () -> pdp.bootstrap(new PMLBootstrapper(
            "u1", "create pc \"pc1\""
        )));
    }

}