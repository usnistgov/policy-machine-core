package gov.nist.csd.pm.pap.function.op.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_ADMIN_OPERATION;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

public class DeleteAdminOperationOp extends Operation<Void, DeleteAdminOperationOp.DeleteAdminOperationOpArgs> {

    public static final FormalParameter<String> NAME_ARG = new FormalParameter<>("name", STRING_TYPE);

    public DeleteAdminOperationOp() {
        super(
                "delete_admin_operation",
                List.of(NAME_ARG)
        );
    }

    public static class DeleteAdminOperationOpArgs extends Args {
        private final String name;

        public DeleteAdminOperationOpArgs(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public DeleteAdminOperationOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_ARG, argsMap);
        return new DeleteAdminOperationOpArgs(name);
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, DeleteAdminOperationOpArgs args) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), DELETE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, DeleteAdminOperationOpArgs args) throws PMException {
        pap.modify().operations().deleteAdminOperation(args.getName());
        return null;
    }
}
