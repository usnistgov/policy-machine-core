package gov.nist.csd.pm.pap.pml.function.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionWrapper;
import gov.nist.csd.pm.pap.pml.function.arg.FormalArgWrapper;
import gov.nist.csd.pm.pap.pml.function.arg.WrappedFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import java.util.ArrayList;
import java.util.List;

public class PMLRoutineWrapper extends PMLRoutine implements PMLFunctionWrapper {

    private final Routine<?> routine;
    private final List<WrappedFormalArg<?>> args;

    public PMLRoutineWrapper(Routine<?> routine) {
        super(
            routine.getName(),
            Type.any(),
            new ArrayList<>(FormalArgWrapper.wrap(routine.getFormalArgs()))
        );

        this.routine = routine;
        this.args = FormalArgWrapper.wrap(routine.getFormalArgs());
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        Object o = routine.execute(pap, args);

        return Value.fromObject(o);
    }

    @Override
    public List<WrappedFormalArg<?>> getPMLFormalArgs() {
        return args;
    }
}
