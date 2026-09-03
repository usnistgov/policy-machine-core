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

package gov.nist.ngac.pm.core.pdp.modification;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.epp.EPP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.Routine;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pdp.PDP;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationsModificationAdjudicatorTest {

    PAP pap;
    PDP pdp;
    EPP epp;

    TestEventSubscriber testEventProcessor;
    OperationsModificationAdjudicator ok;
    OperationsModificationAdjudicator fail;


    @BeforeEach
    void setup() throws PMException {
        pap = new TestPAP();

        pap.executePML(NodeUserContext.of("u1"), """
                create pc "pc1"

                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]

                create oa "oa1" in ["pc1"]
                
                associate "ua1" to "oa1" with ["admin:*"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:*"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        testEventProcessor = new TestEventSubscriber();
        pdp.addEventSubscriber(testEventProcessor);

        ok = new OperationsModificationAdjudicator(NodeUserContext.of("u1"), pap);
        fail = new OperationsModificationAdjudicator(NodeUserContext.of(id("u2")), pap);
    }


    @Test
    void setResourceAccessRights() throws PMException {
        assertDoesNotThrow(() -> ok.setResourceAccessRights(new AccessRightSet("read")));
        assertEquals(new AccessRightSet("read"), pap.query().operations().getResourceAccessRights());
        assertThrows(UnauthorizedException.class, () -> fail.setResourceAccessRights(new AccessRightSet("read")));
    }

    @Test
    void createAdminOperation() throws PMException {
        AdminOperation<Void> op1 = new AdminOperation<>("op1", VOID_TYPE, List.of(), List.of()) {
            @Override
            public Void execute(PAP pap, UserContext userCtx, Args actualArgs) throws PMException {
                return null;
            }

        };

        pap.javaOperations().register(op1);
        assertDoesNotThrow(() -> ok.createOperation(op1));
        assertTrue(pap.query().operations().getOperations().contains(op1));
        assertThrows(UnauthorizedException.class, () -> fail.createOperation(op1));
    }

    @Test
    void deleteAdminOperation() throws PMException {
        AdminOperation<Void> op1 = new AdminOperation<>("op1", VOID_TYPE, List.of(), List.of()) {

            @Override
            public Void execute(PAP pap, UserContext userCtx, Args actualArgs) throws PMException {
                return null;
            }

        };
        pap.javaOperations().register(op1);
        ok.createOperation(op1);

        assertDoesNotThrow(() -> ok.deleteOperation("op1"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteOperation("op1"));
    }

    @Test
    void createAdminRoutine() throws PMException {
        Routine<?> routine1 = new Routine<>("routine1", VOID_TYPE, List.of()) {
            @Override
            public Void execute(PAP pap, UserContext userCtx, Args actualArgs) throws PMException {
                return null;
            }

        };

        pap.javaOperations().register(routine1);
        assertDoesNotThrow(() -> ok.createOperation(routine1));
        assertTrue(pap.query().operations().getOperations().contains(routine1));
        assertThrows(UnauthorizedException.class, () -> fail.createOperation(routine1));
    }
}