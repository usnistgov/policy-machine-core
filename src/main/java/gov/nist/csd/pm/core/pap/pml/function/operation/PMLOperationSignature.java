package gov.nist.csd.pm.core.pap.pml.function.operation;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;

import gov.nist.csd.pm.core.pap.pml.type.TypeStringer;
import java.util.List;

public class PMLOperationSignature extends PMLFunctionSignature {

    private boolean isAdminOp;

    public PMLOperationSignature(String name,
                                 Type<?> returnType,
                                 List<FormalParameter<?>> formalParameters,
                                 boolean isAdminOp) {
        super(name, returnType, formalParameters);

        this.isAdminOp = isAdminOp;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return toString(isAdminOp ? "adminop" : "resourceop", indentLevel);
    }

    @Override
    protected String serializeFormalArgs() {
        String pml = "";
        for (FormalParameter<?> formalParameter : getFormalArgs()) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            pml += ((formalParameter instanceof NodeFormalParameter) ? "node " : "") +
                TypeStringer.toPMLString(formalParameter.getType()) + " " +
                formalParameter.getName();
        }
        return pml;
    }
}
