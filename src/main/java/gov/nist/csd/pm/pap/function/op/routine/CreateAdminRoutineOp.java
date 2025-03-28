package gov.nist.csd.pm.pap.function.op.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_ADMIN_ROUTINE;

public class CreateAdminRoutineOp extends Operation<Void> {

    public static final FormalArg<Routine> ROUTINE_ARG = new FormalArg<>("routine", Routine.class);

    public CreateAdminRoutineOp() {
        super(
                "create_admin_routine",
                List.of(ROUTINE_ARG)
        );
    }

    public ActualArgs actualArgs(Routine<?> routine) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(ROUTINE_ARG, routine);
        return actualArgs;
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Routine<?> routine = actualArgs.get(ROUTINE_ARG);
        pap.modify().routines().createAdminRoutine(routine);
        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs actualArgs) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), CREATE_ADMIN_ROUTINE);
    }
}
