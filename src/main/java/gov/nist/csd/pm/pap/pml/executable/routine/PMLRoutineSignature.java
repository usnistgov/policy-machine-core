package gov.nist.csd.pm.pap.pml.executable.routine;

import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;

public class PMLRoutineSignature extends PMLExecutableSignature {

    public PMLRoutineSignature(String name,
                               Type returnType,
                               List<PMLFormalArg> formalArgs) {
        super(name, returnType, formalArgs);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return toString("routine", indentLevel);
    }
}
