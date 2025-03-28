package gov.nist.csd.pm.pap.function.op.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_ADMIN_OPERATION;

public class CreateAdminOperationOp extends Operation<Void> {

    public static final FormalArg<Operation> OPERATION_ARG = new FormalArg<>("operation", Operation.class);

    public CreateAdminOperationOp() {
        super(
                "create_admin_operation",
                List.of(OPERATION_ARG)
        );
    }

    public ActualArgs actualArgs(Operation<?> operation) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(OPERATION_ARG, operation);
        return actualArgs;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs actualArgs) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), CREATE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Operation<?> operation = actualArgs.get(OPERATION_ARG);
        pap.modify().operations().createAdminOperation(operation);
        return null;
    }
}
