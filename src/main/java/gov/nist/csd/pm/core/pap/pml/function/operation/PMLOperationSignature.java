package gov.nist.csd.pm.core.pap.pml.function.operation;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.type.TypeStringer;
import java.util.List;
import java.util.stream.Collectors;

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
        for (FormalParameter<?> formalParameter : getFormalParameters()) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            String paramStr = "%s%s %s";
            String annotationStr = "";
            if (formalParameter instanceof NodeFormalParameter<?> nodeFormalParameter) {
                AccessRightSet reqCaps = nodeFormalParameter.getReqCap().getReqCaps();

                annotationStr = "@node" +
                    (!reqCaps.isEmpty() ?
                        "(" + reqCaps.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")) + ") "
                        : " ");
            }

            pml += String.format(paramStr, annotationStr, TypeStringer.toPMLString(formalParameter.getType()), formalParameter.getName());
        }
        return pml;
    }
}
