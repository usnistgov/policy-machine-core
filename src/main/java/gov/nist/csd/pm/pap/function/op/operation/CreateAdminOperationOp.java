package gov.nist.csd.pm.pap.function.op.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.arg.type.OperationType;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_ADMIN_OPERATION;

public class CreateAdminOperationOp extends Operation<Void> {

    public static final FormalArg<Operation<?>> OPERATION_ARG = new FormalArg<>("operation", new OperationType());

    public CreateAdminOperationOp() {
        super(
                "create_admin_operation",
                List.of(OPERATION_ARG)
        );
    }

    public Args actualArgs(Operation<?> operation) {
        Args args = new Args();
        args.put(OPERATION_ARG, operation);
        return args;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), CREATE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        Operation<?> operation = args.get(OPERATION_ARG);
        pap.modify().operations().createAdminOperation(operation);
        return null;
    }
}
