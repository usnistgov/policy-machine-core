package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.modification.RoutinesModifier;
import gov.nist.csd.pm.pap.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.op.routine.DeleteAdminRoutineOp;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.routine.Routine;

import java.util.Map;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.op.routine.CreateAdminRoutineOp.ROUTINE_OPERAND;

public class RoutinesModificationAdjudicator extends RoutinesModifier {

    private final UserContext userCtx;
    private final PAP pap;
    private final EventEmitter eventEmitter;

    public RoutinesModificationAdjudicator(UserContext userCtx, PAP pap, EventEmitter eventEmitter) {
        super(pap.modify());
        this.userCtx = userCtx;
        this.pap = pap;
        this.eventEmitter = eventEmitter;
    }

    @Override
    public void createAdminRoutine(Routine<?> routine) throws PMException {
        EventContext event = new CreateAdminRoutineOp()
                .withOperands(Map.of(ROUTINE_OPERAND, routine))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void deleteAdminRoutine(String name) throws PMException {
        EventContext event = new DeleteAdminRoutineOp()
                .withOperands(Map.of(NAME_OPERAND, name))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }
}
