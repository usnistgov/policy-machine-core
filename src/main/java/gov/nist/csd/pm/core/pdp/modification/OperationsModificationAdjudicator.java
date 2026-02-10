package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.operation.Operation.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.OperationsModification;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.operation.CreateOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.DeleteOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.SetResourceAccessRights;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;
import java.util.ArrayList;

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
        op.execute(pap, args);
    }

    @Override
    public void createOperation(Operation<?> operation) throws PMException {
        CreateOperationOp op = new CreateOperationOp();
        Args args = new Args()
            .put(CreateOperationOp.OPERATION_PARAM, operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteOperation(String name) throws PMException {
        DeleteOperationOp op = new DeleteOperationOp();
        Args args = new Args()
            .put(NAME_PARAM, name);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }
}
