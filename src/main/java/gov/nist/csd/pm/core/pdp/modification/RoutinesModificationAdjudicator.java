package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.function.op.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.routine.CreateAdminRoutineOp.ROUTINE_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.core.pap.function.op.routine.DeleteAdminRoutineOp;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.RoutinesModification;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;

public class RoutinesModificationAdjudicator extends Adjudicator implements RoutinesModification {

    public RoutinesModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createAdminRoutine(Routine<?> routine) throws PMException {
        CreateAdminRoutineOp op = new CreateAdminRoutineOp();
        Args args = new Args()
            .put(ROUTINE_PARAM, routine);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteAdminRoutine(String name) throws PMException {
        DeleteAdminRoutineOp op = new DeleteAdminRoutineOp();
        Args args = new Args()
            .put(NAME_PARAM, name);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }
}
