package gov.nist.csd.pm.core.pap.function.op.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_ADMIN_OPERATION;

public class DeleteAdminOperationOp extends Operation<Void> {

    public DeleteAdminOperationOp() {
        super(
                "delete_admin_operation",
                List.of(NAME_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(), DELETE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().deleteAdminOperation(args.get(NAME_PARAM));
        return null;
    }
}
