package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.common.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.common.op.operation.DeleteAdminOperationOp;
import gov.nist.csd.pm.common.op.operation.SetResourceOperationsOp;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.common.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.common.op.operation.CreateAdminOperationOp.OPERATION_OPERAND;
import static gov.nist.csd.pm.common.op.operation.SetResourceOperationsOp.OPERATIONS_OPERAND;
import static gov.nist.csd.pm.util.TestMemoryPAP.id;
import static org.junit.jupiter.api.Assertions.*;

class OperationsModificationAdjudicatorTest {

    PAP pap;
    PDP pdp;
    EPP epp;

    TestEventSubscriber testEventProcessor;
    OperationsModificationAdjudicator ok;
    OperationsModificationAdjudicator fail;


    @BeforeEach
    void setup() throws PMException {
        pap = new MemoryPAP();

        pap.executePML(new TestUserContext("u1", pap), """
                create pc "pc1"

                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]

                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);

        testEventProcessor = new TestEventSubscriber();
        pdp.addEventSubscriber(testEventProcessor);

        ok = new OperationsModificationAdjudicator(new UserContext(id(pap, "u1")), pap, pdp.getPrivilegeChecker());
        fail = new OperationsModificationAdjudicator(new UserContext(id(pap, "u2")), pap, pdp.getPrivilegeChecker());
    }


    @Test
    void setResourceOperations() throws PMException {
        assertDoesNotThrow(() -> ok.setResourceOperations(new AccessRightSet("read")));
        assertEquals(
                new EventContext("u1", null, new SetResourceOperationsOp(), Map.of(OPERATIONS_OPERAND, new AccessRightSet("read"))),
                testEventProcessor.getEventContext()
        );
        assertEquals(new AccessRightSet("read"), pap.query().operations().getResourceOperations());
        assertThrows(UnauthorizedException.class, () -> fail.setResourceOperations(new AccessRightSet("read")));
    }

    @Test
    void createAdminOperation() throws PMException {
        Operation<Void> op1 = new Operation<>("op1") {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

            }

            @Override
            public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                return null;
            }
        };

        assertDoesNotThrow(() -> ok.createAdminOperation(op1));
        assertEquals(
                new EventContext("u1", null, new CreateAdminOperationOp(), Map.of(OPERATION_OPERAND, op1)),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().operations().getAdminOperationNames().contains("op1"));
        assertThrows(UnauthorizedException.class, () -> fail.createAdminOperation(op1));
    }

    @Test
    void deleteAdminOperation() throws PMException {
        Operation<Void> op1 = new Operation<>("op1") {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

            }

            @Override
            public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                return null;
            }
        };
        ok.createAdminOperation(op1);

        assertDoesNotThrow(() -> ok.deleteAdminOperation("op1"));
        assertEquals(
                new EventContext("u1", null, new DeleteAdminOperationOp(), Map.of(NAME_OPERAND, "op1")),
                testEventProcessor.getEventContext()
        );

        assertThrows(UnauthorizedException.class, () -> fail.deleteAdminOperation("op1"));
    }
}