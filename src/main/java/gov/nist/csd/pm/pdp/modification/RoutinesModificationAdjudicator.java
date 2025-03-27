package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.executable.op.routine.DeleteAdminRoutineOp;
import gov.nist.csd.pm.pap.executable.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.modification.RoutinesModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

public class RoutinesModificationAdjudicator extends Adjudicator implements RoutinesModification {

    private final UserContext userCtx;
    private final PAP pap;

    public RoutinesModificationAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createAdminRoutine(Routine<?> routine) throws PMException {
        CreateAdminRoutineOp op = new CreateAdminRoutineOp();
        ActualArgs args = op.actualArgs(routine);

        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteAdminRoutine(String name) throws PMException {
        DeleteAdminRoutineOp op = new DeleteAdminRoutineOp();
        ActualArgs args = op.actualArgs(name);

        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);
    }
}
