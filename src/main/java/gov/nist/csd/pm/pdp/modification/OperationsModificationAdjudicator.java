package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.modification.OperationsModifier;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.pap.op.operation.DeleteAdminOperationOp;
import gov.nist.csd.pm.pap.op.operation.SetResourceOperationsOp;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.Map;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.op.operation.CreateAdminOperationOp.OPERATION_OPERAND;
import static gov.nist.csd.pm.pap.op.operation.SetResourceOperationsOp.OPERATIONS_OPERAND;

public class OperationsModificationAdjudicator extends OperationsModifier {

    private final UserContext userCtx;
    private final PAP pap;
    private final EventEmitter eventEmitter;

    public OperationsModificationAdjudicator(UserContext userCtx, PAP pap, EventEmitter eventEmitter) throws PMException {
        super(pap.modify());
        this.userCtx = userCtx;
        this.pap = pap;
        this.eventEmitter = eventEmitter;
    }

    @Override
    public void setResourceOperations(AccessRightSet accessRightSet) throws PMException {
        EventContext event = new SetResourceOperationsOp()
                .withOperands(Map.of(OPERATIONS_OPERAND, accessRightSet))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void createAdminOperation(Operation<?> operation) throws PMException {
        EventContext event = new CreateAdminOperationOp()
                .withOperands(Map.of(OPERATION_OPERAND, operation))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void deleteAdminOperation(String operation) throws PMException {
        EventContext event = new DeleteAdminOperationOp()
                .withOperands(Map.of(NAME_OPERAND, operation))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }
}
