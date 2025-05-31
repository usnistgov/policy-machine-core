package gov.nist.csd.pm.core.pap.function.op.routine;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_ADMIN_ROUTINE;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;

public class CreateAdminRoutineOp extends Operation<Void, CreateAdminRoutineOp.CreateAdminRoutineOpArgs> {

    public static final FormalParameter<Object> ROUTINE_PARAM = new FormalParameter<>("routine", ANY_TYPE);

    public CreateAdminRoutineOp() {
        super(
                "create_admin_routine",
                List.of(ROUTINE_PARAM)
        );
    }

    public static class CreateAdminRoutineOpArgs extends Args {
        private final Routine<?, ?> routine;

        public CreateAdminRoutineOpArgs(Routine<?, ?> routine) {
            super(Map.of(
                ROUTINE_PARAM, routine
            ));

            this.routine = routine;
        }

        public Routine<?, ?> getRoutine() {
            return routine;
        }
    }

    @Override
    protected CreateAdminRoutineOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        Routine<?, ?> routine = (Routine<?, ?>) prepareArg(ROUTINE_PARAM, argsMap);
        return new CreateAdminRoutineOpArgs(routine);
    }

    @Override
    public Void execute(PAP pap, CreateAdminRoutineOpArgs args) throws PMException {
        pap.modify().routines().createAdminRoutine(args.getRoutine());
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, CreateAdminRoutineOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_ROUTINES.nodeId(), CREATE_ADMIN_ROUTINE);
    }
}
