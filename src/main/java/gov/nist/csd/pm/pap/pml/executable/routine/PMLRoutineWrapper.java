package gov.nist.csd.pm.pap.pml.executable.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.executable.arg.FormalArgWrapper;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

public class PMLRoutineWrapper extends PMLRoutine {

    private final Routine<?> routine;

    public PMLRoutineWrapper(Routine<?> routine) {
        super(
            routine.getName(),
            Type.any(),
            FormalArgWrapper.wrap(routine.getFormalArgs())
        );

        this.routine = routine;
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Object o = routine.execute(pap, actualArgs);

        return Value.fromObject(o);
    }
}
