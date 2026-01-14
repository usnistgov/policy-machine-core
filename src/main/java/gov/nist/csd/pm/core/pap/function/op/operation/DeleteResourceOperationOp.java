package gov.nist.csd.pm.core.pap.function.op.operation;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_RESOURCE_OPERATION;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class DeleteResourceOperationOp extends AdminOperation<Void> {

    public DeleteResourceOperationOp() {
        super(
            "delete_resource_operation",
            List.of(NAME_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(), DELETE_RESOURCE_OPERATION);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().deleteResourceOperation(args.get(NAME_PARAM));
        return null;
    }
}
