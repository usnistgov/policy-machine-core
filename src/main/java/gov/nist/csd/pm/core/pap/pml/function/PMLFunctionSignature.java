package gov.nist.csd.pm.core.pap.pml.function;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.pml.type.TypeStringer;
import java.util.List;
import java.util.Objects;

public abstract class PMLFunctionSignature implements PMLStatementSerializable {

    protected String name;
    protected Type<?> returnType;
    protected List<FormalParameter<?>> formalParameters;

    public PMLFunctionSignature(String name, Type<?> returnType, List<FormalParameter<?>> formalParameters) {
        this.name = name;
        this.returnType = returnType;
        this.formalParameters = formalParameters;
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
        for (FormalParameter<?> formalParameter : formalParameters) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            pml += TypeStringer.toPMLString(formalParameter.getType()) + " " + formalParameter.getName();
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PMLFunctionSignature signature))
            return false;
        return Objects.equals(name, signature.name) && Objects.equals(returnType,
            signature.returnType) && Objects.equals(formalParameters, signature.formalParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, formalParameters);
    }
}
