package gov.nist.csd.pm.pap.pml.function.routine;

import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;

import java.util.List;

public class PMLRoutineSignature extends PMLFunctionSignature {

    public PMLRoutineSignature(String name,
                               Type<?> returnType,
                               List<FormalParameter<?>> formalParameters) {
        super(name, returnType, formalParameters);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return toString("routine", indentLevel);
    }
}
