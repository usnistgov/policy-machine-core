package gov.nist.csd.pm.common.op.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_ADMIN_OPERATION;

public class DeleteAdminOperationOp extends Operation<Void> {

    public DeleteAdminOperationOp() {
        super(
                "delete_admin_operation",
                List.of(NAME_OPERAND)
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), DELETE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        String name = (String) operands.get(NAME_OPERAND);
        pap.modify().operations().deleteAdminOperation(name);

        return null;
    }
}
