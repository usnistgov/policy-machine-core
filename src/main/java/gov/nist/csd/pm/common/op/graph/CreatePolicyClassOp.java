package gov.nist.csd.pm.common.op.graph;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_POLICY_CLASS;

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
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), CREATE_POLICY_CLASS);
    }

    @Override
    public Long execute(PAP pap, Map<String, Object> operands) throws PMException {
        return pap.modify().graph().createPolicyClass(
                (String) operands.get(NAME_OPERAND)
        );
    }

    public static class EventCtx extends EventContext {

        public EventCtx(String user, String process, String name) {
            super(user, process, "create_policy_class", Map.of(
                    NAME_OPERAND, name
            ));
        }
    }
}
