package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.op.routine.DeleteAdminRoutineOp;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.modification.RoutinesModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Map;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.op.routine.CreateAdminRoutineOp.ROUTINE_OPERAND;

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
        new CreateAdminRoutineOp()
                .withOperands(Map.of(ROUTINE_OPERAND, routine))
                .execute(pap, userCtx, privilegeChecker);
    }

    @Override
    public void deleteAdminRoutine(String name) throws PMException {
        new DeleteAdminRoutineOp()
                .withOperands(Map.of(NAME_OPERAND, name))
                .execute(pap, userCtx, privilegeChecker);
    }
}
