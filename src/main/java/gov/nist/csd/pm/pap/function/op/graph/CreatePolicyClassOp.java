package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_POLICY_CLASS;

public class CreatePolicyClassOp extends CreateNodeOp{

    public CreatePolicyClassOp() {
        super(
                "create_policy_class",
                List.of(NAME_ARG),
                CREATE_POLICY_CLASS
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), CREATE_POLICY_CLASS);
    }

    @Override
    public Long execute(PAP pap, ActualArgs actualArgs) throws PMException {
        return pap.modify().graph().createPolicyClass(
		        actualArgs.get(NAME_ARG)
        );
    }
}
