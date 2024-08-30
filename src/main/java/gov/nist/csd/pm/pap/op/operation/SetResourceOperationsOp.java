package gov.nist.csd.pm.pap.op.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.SET_RESOURCE_OPERATIONS;

public class SetResourceOperationsOp extends Operation<Void> {

    public static final String OPERATIONS_OPERAND = "operations";

    public SetResourceOperationsOp() {
        super(
                "set_resource_operations",
                List.of(OPERATIONS_OPERAND)
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName(), SET_RESOURCE_OPERATIONS);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().operations().setResourceOperations((AccessRightSet) operands.get(OPERATIONS_OPERAND));

        return null;
    }
}
