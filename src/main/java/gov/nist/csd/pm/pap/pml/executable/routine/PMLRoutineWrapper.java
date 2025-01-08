package gov.nist.csd.pm.pap.pml.executable.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.common.routine.Routine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PMLRoutineWrapper extends PMLRoutine {

    private Routine<?> routine;

    public PMLRoutineWrapper(Routine<?> routine) {
        super(
                routine.getName(),
                Type.any(),
                routine.getOperandNames(),
                getTypesFromOperandNames(routine.getOperandNames())
        );

        this.routine = routine;
    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Object o = routine.execute(pap, operands);

        return Value.fromObject(o);
    }

    public static Map<String, Type> getTypesFromOperandNames(List<String> operandNames) {
        Map<String, Type> types = new HashMap<>();
        for (String operandName : operandNames) {
            types.put(operandName, Type.any());
        }

        return types;
    }
}
