package gov.nist.csd.pm.pap.pml.function.operation;

import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;

public class PMLOperationSignature extends PMLFunctionSignature {

    public PMLOperationSignature(String functionName,
                                 Type returnType,
                                 List<PMLFormalArg> formalArgs) {
        super(functionName, returnType, formalArgs);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return toString("operation", indentLevel);
    }

    @Override
    protected String serializeFormalArgs() {
        String pml = "";
        for (PMLFormalArg formalArg : getFormalArgs()) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            pml += ((formalArg instanceof PMLNodeFormalArg) ? "@node " : "") +  formalArg.getPmlType().toString() + " " + formalArg.getName();
        }
        return pml;
    }
}
