package gov.nist.csd.pm.policy.pml.function;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.List;
import java.util.Objects;

public class FunctionSignature extends PMLStatement {

    private String functionName;
    private Type returnType;
    private List<FormalArgument> args;

    public FunctionSignature(String functionName, Type returnType, List<FormalArgument> args) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.args = args;
    }

    public FunctionSignature(PMLParser.FunctionSignatureContext ctx) {
        super(ctx);
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public List<FormalArgument> getArgs() {
        return args;
    }

    public void setArgs(List<FormalArgument> args) {
        this.args = args;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new VoidValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FunctionSignature that = (FunctionSignature) o;
        return Objects.equals(functionName, that.functionName) && Objects.equals(
                returnType, that.returnType) && Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, returnType, args);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String argsStr = serializeFormalArgs();

        String indent = indent(indentLevel);
        return String.format(
                "%sfunction %s(%s) %s",
                indent,
                functionName,
                argsStr,
                returnType.isVoid() ? "" : returnType.toString() + " "
        );
    }

    private String serializeFormalArgs() {
        String pml = "";
        for (FormalArgument formalArgument : args) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            pml += formalArgument.type().toString() + " " + formalArgument.name();
        }
        return pml;
    }
}
