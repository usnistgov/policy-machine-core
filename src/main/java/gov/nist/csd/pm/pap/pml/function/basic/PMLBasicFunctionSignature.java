package gov.nist.csd.pm.pap.pml.function.basic;

import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;

public class PMLBasicFunctionSignature extends PMLFunctionSignature {


    public PMLBasicFunctionSignature(String name, Type returnType, List<PMLFormalArg> formalArgs) {
        super(name, returnType, formalArgs);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return toString("function", indentLevel);
    }
}
