package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;

public class PMLOperationSignature extends PMLExecutableSignature {

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
