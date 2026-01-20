package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.operation.Operation.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.operation.operation.CreateAdminOperationOp.ADMIN_OPERATION_PARAM;
import static gov.nist.csd.pm.core.pap.operation.operation.CreateAdminRoutineOp.ROUTINE_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.BasicFunction;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.CreateAdminRoutineOp;
import gov.nist.csd.pm.core.pap.operation.operation.CreateBasicFunctionOp;
import gov.nist.csd.pm.core.pap.operation.operation.CreateQueryOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.CreateResourceOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.DeleteOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.SetResourceAccessRights;
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
    public void createResourceOperation(ResourceOperation<?> operation) throws PMException {
        CreateResourceOperationOp op = new CreateResourceOperationOp();
        Args args = new Args()
            .put(CreateResourceOperationOp.OPERATION_PARAM, operation);

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
    public void createAdminRoutine(Routine<?> routine) throws PMException {
        CreateAdminRoutineOp op = new CreateAdminRoutineOp();
        Args args = new Args()
            .put(ROUTINE_PARAM, routine);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void createQueryOperation(QueryOperation<?> operation) throws PMException {
        CreateAdminRoutineOp op = new CreateAdminRoutineOp();
        Args args = new Args()
            .put(CreateQueryOperationOp.QUERY_OPERATION_PARAM, operation);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void createBasicFunction(BasicFunction<?> function) throws PMException {
        CreateBasicFunctionOp op = new CreateBasicFunctionOp();
        Args args = new Args()
            .put(CreateBasicFunctionOp.BASIC_FUNCTION_PARAM, function);

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
