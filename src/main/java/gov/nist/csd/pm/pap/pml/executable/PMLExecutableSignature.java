package gov.nist.csd.pm.pap.pml.executable;

import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;
import java.util.Objects;

public abstract class PMLExecutableSignature implements PMLStatementSerializable {

    protected String name;
    protected Type returnType;
    protected List<PMLFormalArg> formalArgs;

    public PMLExecutableSignature(String name, Type returnType, List<PMLFormalArg> formalArgs) {
        this.name = name;
        this.returnType = returnType;
        this.formalArgs = formalArgs;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<PMLFormalArg> getFormalArgs() {
        return formalArgs;
    }

    protected String serializeFormalArgs() {
        String pml = "";
        for (PMLFormalArg formalArg : formalArgs) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            pml += formalArg.getPmlType().toString() + " " + formalArg.getName();
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
            returnType.isVoid() ? "" : returnType.toString() + " "
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PMLExecutableSignature signature))
            return false;
        return Objects.equals(name, signature.name) && Objects.equals(returnType,
            signature.returnType) && Objects.equals(formalArgs, signature.formalArgs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, formalArgs);
    }
}
