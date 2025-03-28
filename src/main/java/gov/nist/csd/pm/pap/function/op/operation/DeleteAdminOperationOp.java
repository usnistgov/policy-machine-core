package gov.nist.csd.pm.pap.function.op.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_ADMIN_OPERATION;

public class DeleteAdminOperationOp extends Operation<Void> {

    public DeleteAdminOperationOp() {
        super(
                "delete_admin_operation",
                List.of(NAME_ARG)
        );
    }

    public ActualArgs actualArgs(String name) {
        ActualArgs args = new ActualArgs();
        args.put(NAME_ARG, name);
        return args;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs actualArgs) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), DELETE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        String name = actualArgs.get(NAME_ARG);
        pap.modify().operations().deleteAdminOperation(name);

        return null;
    }
}
