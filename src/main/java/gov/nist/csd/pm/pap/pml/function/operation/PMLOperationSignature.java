package gov.nist.csd.pm.pap.pml.function.operation;

import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;

import gov.nist.csd.pm.pap.pml.function.arg.ArgTypeStringer;
import java.util.List;

public class PMLOperationSignature extends PMLFunctionSignature {

    public PMLOperationSignature(String name,
                                 Type<?> returnType,
                                 List<FormalParameter<?>> formalParameters) {
        super(name, returnType, formalParameters);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return toString("operation", indentLevel);
    }

    @Override
    protected String serializeFormalArgs() {
        String pml = "";
        for (FormalParameter<?> formalParameter : getFormalArgs()) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            pml += ((formalParameter instanceof NodeFormalParameter<?>) ? "@node " : "") +
                ArgTypeStringer.toPMLString(formalParameter.getType()) + " " +
                formalParameter.getName();
        }
        return pml;
    }
}
