package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.op.operation.*;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.operation.CreateAdminOperationOp.CreateAdminOperationOpArgs;
import gov.nist.csd.pm.pap.function.op.operation.DeleteAdminOperationOp.DeleteAdminOperationOpArgs;
import gov.nist.csd.pm.pap.function.op.operation.SetResourceOperationsOp.SetResourceOperationsOpArgs;
import gov.nist.csd.pm.pap.modification.OperationsModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

public class OperationsModificationAdjudicator extends Adjudicator implements OperationsModification {

    public OperationsModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void setResourceOperations(AccessRightSet accessRightSet) throws PMException {
        SetResourceOperationsOp op = new SetResourceOperationsOp();
        SetResourceOperationsOpArgs args = new SetResourceOperationsOpArgs(accessRightSet);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void createAdminOperation(Operation<?, ?> operation) throws PMException {
        CreateAdminOperationOp op = new CreateAdminOperationOp();
        CreateAdminOperationOpArgs args = new CreateAdminOperationOpArgs(operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteAdminOperation(String operation) throws PMException {
        DeleteAdminOperationOp op = new DeleteAdminOperationOp();
        DeleteAdminOperationOpArgs args = new DeleteAdminOperationOpArgs(operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }
}
