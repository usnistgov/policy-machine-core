package gov.nist.csd.pm.common.op.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.routine.Routine;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_ADMIN_ROUTINE;

public class CreateAdminRoutineOp extends Operation<Void> {

    public static final String ROUTINE_OPERAND = "routine";

    public CreateAdminRoutineOp() {
        super(
                "create_admin_routine",
                List.of(ROUTINE_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        Routine<?> routine = (Routine<?>) operands.get(ROUTINE_OPERAND);

        pap.modify().routines().createAdminRoutine(routine);

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), CREATE_ADMIN_ROUTINE);
    }
}
