package gov.nist.csd.pm.core.pap.pml.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.pml.type.TypeStringer;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PMLOperationSignature implements PMLStatementSerializable {

    private OperationType type;
    private String name;
    private Type<?> returnType;
    private List<FormalParameter<?>> formalParameters;

    public PMLOperationSignature(OperationType type, String name, Type<?> returnType, List<FormalParameter<?>> formalParameters) {
        this.type = type;
        this.name = name;
        this.returnType = returnType;
        this.formalParameters = formalParameters;
    }

    public OperationType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Type<?> getReturnType() {
        return returnType;
    }

    public List<FormalParameter<?>> getFormalParameters() {
        return formalParameters;
    }

    protected String serializeFormalArgs() {
        String pml = "";
        for (FormalParameter<?> formalParameter : getFormalParameters()) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            String annotationStr = "";
            if (formalParameter instanceof NodeFormalParameter<?> nodeFormalParameter) {
                AccessRightSet reqCaps = nodeFormalParameter.getRequiredCapabilities();

                annotationStr = "@node" +
                    (!reqCaps.isEmpty() ?
                        "(" + reqCaps.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")) + ") "
                        : " ");
            }

            pml += String.format("%s%s %s", annotationStr, TypeStringer.toPMLString(formalParameter.getType()),
                formalParameter.getName());
        }
        return pml;
    }

    protected String toString(String prefix, int indentLevel) {
        String argsStr = serializeFormalArgs();

        String indent = indent(indentLevel);
        return String.format(
            "%s%s %s(%s) %s",
            indent,
            prefix,
            name,
            argsStr,
            returnType == null || returnType.equals(VOID_TYPE) ? "" : TypeStringer.toPMLString(returnType) + " "
        );
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return toString(type.toString().toLowerCase(), indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PMLOperationSignature signature))
            return false;
        return Objects.equals(name, signature.name) && Objects.equals(returnType,
            signature.returnType) && Objects.equals(formalParameters, signature.formalParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, formalParameters);
    }

    public enum OperationType {
        ADMINOP,
        RESOURCEOP,
        QUERY,
        FUNCTION,
        ROUTINE
    }
}
