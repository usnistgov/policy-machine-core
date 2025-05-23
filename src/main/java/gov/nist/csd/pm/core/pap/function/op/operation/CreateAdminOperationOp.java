package gov.nist.csd.pm.core.pap.function.op.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_ADMIN_OPERATION;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;

public class CreateAdminOperationOp extends Operation<Void, CreateAdminOperationOp.CreateAdminOperationOpArgs> {

    public static final FormalParameter<Object> OPERATION_PARAM = new FormalParameter<>("operation", ANY_TYPE);

    public CreateAdminOperationOp() {
        super(
                "create_admin_operation",
                List.of(OPERATION_PARAM)
        );
    }

    public static class CreateAdminOperationOpArgs extends Args {
        private final Operation<?, ?> operation;

        public CreateAdminOperationOpArgs(Operation<?, ?> operation) {
            super(Map.of(
                OPERATION_PARAM, operation
            ));

            this.operation = operation;
        }

        public Operation<?, ?> getOperation() {
            return operation;
        }
    }

    @Override
    protected CreateAdminOperationOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        Operation<?, ?> operation = (Operation<?, ?>) prepareArg(OPERATION_PARAM, argsMap);
        return new CreateAdminOperationOpArgs(operation);
    }

    @Override
    public void canExecute(PAP pap,
                           UserContext userCtx, CreateAdminOperationOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), CREATE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, CreateAdminOperationOpArgs args) throws PMException {
        pap.modify().operations().createAdminOperation(args.getOperation());
        return null;
    }
}
