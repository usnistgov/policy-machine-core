package gov.nist.csd.pm.core.pap.pml.function.basic;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import java.util.List;

public class PMLBasicFunctionSignature extends PMLFunctionSignature {

    public PMLBasicFunctionSignature(String name,
                                     Type<?> returnType,
                                     List<FormalParameter<?>> formalParameters) {
        super(name, returnType, formalParameters);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return toString("function", indentLevel);
    }
}
