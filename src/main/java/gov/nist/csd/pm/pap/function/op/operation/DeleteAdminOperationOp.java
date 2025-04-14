package gov.nist.csd.pm.pap.function.op.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.DELETE_ADMIN_OPERATION;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

public class DeleteAdminOperationOp extends Operation<Void, DeleteAdminOperationOp.DeleteAdminOperationOpArgs> {

    public DeleteAdminOperationOp() {
        super(
                "delete_admin_operation",
                List.of(NAME_PARAM)
        );
    }

    public static class DeleteAdminOperationOpArgs extends Args {
        private final String name;

        public DeleteAdminOperationOpArgs(String name) {
            super(Map.of(
                NAME_PARAM, name
            ));

            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    protected DeleteAdminOperationOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_PARAM, argsMap);
        return new DeleteAdminOperationOpArgs(name);
    }

    @Override
    public void canExecute(PAP pap,
                           UserContext userCtx, DeleteAdminOperationOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), DELETE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, DeleteAdminOperationOpArgs args) throws PMException {
        pap.modify().operations().deleteAdminOperation(args.getName());
        return null;
    }
}
