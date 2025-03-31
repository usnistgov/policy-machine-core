package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.op.operation.*;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.modification.OperationsModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

public class OperationsModificationAdjudicator extends Adjudicator implements OperationsModification {

    private final UserContext userCtx;
    private final PAP pap;

    public OperationsModificationAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) throws PMException {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void setResourceOperations(AccessRightSet accessRightSet) throws PMException {
        SetResourceOperationsOp op = new SetResourceOperationsOp();
        Args args = op.actualArgs(accessRightSet);

        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void createAdminOperation(Operation<?> operation) throws PMException {
        CreateAdminOperationOp op = new CreateAdminOperationOp();
        Args args = op.actualArgs(operation);

        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteAdminOperation(String operation) throws PMException {
        DeleteAdminOperationOp op = new DeleteAdminOperationOp();
        Args args = op.actualArgs(operation);

        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);
    }
}
