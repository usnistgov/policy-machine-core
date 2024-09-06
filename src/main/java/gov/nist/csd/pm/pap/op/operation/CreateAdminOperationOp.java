package gov.nist.csd.pm.pap.op.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.*;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.CREATE_ADMIN_OPERATION;

public class CreateAdminOperationOp extends Operation<Void> {

    public static final String OPERATION_OPERAND = "operation";

    public CreateAdminOperationOp() {
        super(
                "create_admin_operation",
                List.of(OPERATION_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        Operation<?> operation = (Operation<?>) operands.get(OPERATION_OPERAND);

        pap.modify().operations().createAdminOperation(operation);

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), CREATE_ADMIN_OPERATION);
    }
}
