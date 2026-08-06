package gov.nist.ngac.pm.core.pdp.modification;

import static gov.nist.ngac.pm.core.pap.operation.Operation.ARSET_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.Operation.NAME_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.modification.OperationsModification;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.operation.CreateOperationOp;
import gov.nist.ngac.pm.core.pap.operation.operation.DeleteOperationOp;
import gov.nist.ngac.pm.core.pap.operation.operation.SetResourceAccessRights;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.adjudication.Adjudicator;
import java.util.ArrayList;

/**
 * A {@link OperationsModification} that checks the acting user's admin privileges before delegating to
 * the PAP.
 */
public class OperationsModificationAdjudicator extends Adjudicator implements OperationsModification {

    public OperationsModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
        SetResourceAccessRights op = new SetResourceAccessRights();
        Args args = new Args()
            .put(ARSET_PARAM, new ArrayList<>(resourceAccessRights));

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }

    @Override
    public void createOperation(Operation<?> operation) throws PMException {
        CreateOperationOp op = new CreateOperationOp();
        Args args = new Args()
            .put(CreateOperationOp.OPERATION_PARAM, operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }

    @Override
    public void deleteOperation(String name) throws PMException {
        DeleteOperationOp op = new DeleteOperationOp();
        Args args = new Args()
            .put(NAME_PARAM, name);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }
}
