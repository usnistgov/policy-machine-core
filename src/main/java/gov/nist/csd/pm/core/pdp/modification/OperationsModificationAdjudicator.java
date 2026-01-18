package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.function.Operation.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.function.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.operation.CreateAdminOperationOp.ADMIN_OPERATION_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.core.pap.function.op.operation.CreateResourceOperationOp;
import gov.nist.csd.pm.core.pap.function.op.operation.DeleteAdminOperationOp;
import gov.nist.csd.pm.core.pap.function.op.operation.DeleteResourceOperationOp;
import gov.nist.csd.pm.core.pap.function.op.operation.SetResourceAccessRights;
import gov.nist.csd.pm.core.pap.modification.OperationsModification;
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
    public void createResourceOperation(ResourceOperation operation) throws PMException {
        CreateResourceOperationOp op = new CreateResourceOperationOp();
        Args args = new Args()
            .put(CreateResourceOperationOp.OPERATION_PARAM, operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteResourceOperation(String operation) throws PMException {
        DeleteResourceOperationOp op = new DeleteResourceOperationOp();
        Args args = new Args()
            .put(NAME_PARAM, operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void createAdminOperation(AdminOperation<?> operation) throws PMException {
        CreateAdminOperationOp op = new CreateAdminOperationOp();
        Args args = new Args()
            .put(ADMIN_OPERATION_PARAM, operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteAdminOperation(String operation) throws PMException {
        DeleteAdminOperationOp op = new DeleteAdminOperationOp();
        Args args = new Args()
            .put(NAME_PARAM, operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }
}
