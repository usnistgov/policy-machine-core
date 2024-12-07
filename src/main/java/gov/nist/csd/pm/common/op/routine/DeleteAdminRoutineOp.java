package gov.nist.csd.pm.common.op.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_ADMIN_ROUTINE;

public class DeleteAdminRoutineOp extends Operation<Void> {

    public DeleteAdminRoutineOp() {
        super(
                "delete_admin_routine",
                List.of(NAME_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        String name = (String) operands.get(NAME_OPERAND);

        pap.modify().routines().deleteAdminRoutine(name);

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), DELETE_ADMIN_ROUTINE);
    }
}
