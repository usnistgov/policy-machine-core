package gov.nist.csd.pm.core.pap.pml.function.query;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import java.util.List;

public class PMLQueryFunctionSignature extends PMLFunctionSignature {

    public PMLQueryFunctionSignature(String name,
                                     Type<?> returnType,
                                     List<FormalParameter<?>> formalParameters) {
        super(name, returnType, formalParameters);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "";
    }
}
