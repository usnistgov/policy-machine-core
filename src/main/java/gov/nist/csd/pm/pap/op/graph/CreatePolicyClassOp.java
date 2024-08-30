package gov.nist.csd.pm.pap.op.graph;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.*;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.CREATE_POLICY_CLASS;

public class CreatePolicyClassOp extends CreateNodeOp{

    public CreatePolicyClassOp() {
        super(
                "create_policy_class",
                List.of(NAME_OPERAND),
                List.of(),
                CREATE_POLICY_CLASS
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName(), CREATE_POLICY_CLASS);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().createPolicyClass(
                (String) operands.get(NAME_OPERAND)
        );

        return null;
    }
}
