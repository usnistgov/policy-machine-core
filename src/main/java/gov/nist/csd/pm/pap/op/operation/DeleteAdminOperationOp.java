package gov.nist.csd.pm.pap.op.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.DELETE_ADMIN_OPERATION;

public class DeleteAdminOperationOp extends Operation<Void> {

    public DeleteAdminOperationOp() {
        super(
                "delete_admin_operation",
                List.of(NAME_OPERAND)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
        PrivilegeChecker.check(pap, userCtx, AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName(), DELETE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        String name = (String) operands.get(NAME_OPERAND);
        pap.modify().operations().deleteAdminOperation(name);

        return null;
    }
}
