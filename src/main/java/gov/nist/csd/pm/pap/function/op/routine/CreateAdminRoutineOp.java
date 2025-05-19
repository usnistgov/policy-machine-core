package gov.nist.csd.pm.pap.function.op.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.CREATE_ADMIN_ROUTINE;

public class CreateAdminRoutineOp extends Operation<Void, CreateAdminRoutineOp.CreateAdminRoutineOpArgs> {

    public static final FormalParameter<Routine<?, ?>> ROUTINE_PARAM = new FormalParameter<>("routine", new RoutineType());

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
        Routine<?, ?> routine = prepareArg(ROUTINE_PARAM, argsMap);
        return new CreateAdminRoutineOpArgs(routine);
    }

    @Override
    public Void execute(PAP pap, CreateAdminRoutineOpArgs args) throws PMException {
        pap.modify().routines().createAdminRoutine(args.getRoutine());
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, CreateAdminRoutineOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), CREATE_ADMIN_ROUTINE);
    }
}
