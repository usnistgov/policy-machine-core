package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionExecutor;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FunctionDefinitionStatement extends PMLStatement {

    private String functionName;
    private Type returnType;
    private List<FormalArgument> args;
    private List<PMLStatement> statements;
    private FunctionExecutor functionExecutor;
    private boolean isFuncExec;

    private FunctionDefinitionStatement() {}

    public FunctionDefinitionStatement(FunctionDefinitionStatement functionDefinitionStatement) {
        this.functionName = functionDefinitionStatement.functionName;
        this.returnType = functionDefinitionStatement.returnType;
        this.args = functionDefinitionStatement.args;
        this.statements = functionDefinitionStatement.statements;
        this.functionExecutor = functionDefinitionStatement.functionExecutor;
        this.isFuncExec = functionDefinitionStatement.isFuncExec;
    }
    private FunctionDefinitionStatement(String functionName, Type returnType, List<FormalArgument> args, List<PMLStatement> stmts) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.args = args;
        this.statements = stmts;
    }

    private FunctionDefinitionStatement(String functionName, Type returnType,
                                       List<FormalArgument> args, FunctionExecutor executor) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.args = args;
        this.functionExecutor = executor;
        this.isFuncExec = true;
    }

    public boolean isFunctionExecutor() {
        return isFuncExec;
    }

    public FunctionExecutor getFunctionExecutor() {
        return functionExecutor;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<FormalArgument> getArgs() {
        return args;
    }

    public List<PMLStatement> getBody() {
        return statements;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMLExecutionException {
        try {
            ctx.scope().addFunction(this);
        } catch (FunctionAlreadyDefinedInScopeException e) {
            throw new PMLExecutionException(e.getMessage());
        }

        return new VoidValue();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        if (isFuncExec) {
            throw new RuntimeException("PML Function " + functionName + " cannot be serialized as it is a Java function");
        }

        String argsStr = serializeFormalArgs();

        String indent = indent(indentLevel);
        return String.format(
                "%sfunction %s(%s) %s%s",
                indent,
                functionName,
                argsStr,
                returnType.isVoid() ? "" : returnType.toString() + " ",
                new PMLStatementBlock(statements).toFormattedString(indentLevel)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionDefinitionStatement that = (FunctionDefinitionStatement) o;
        return isFuncExec == that.isFuncExec &&
                Objects.equals(functionName, that.functionName) &&
                Objects.equals(returnType, that.returnType) &&
                Objects.equals(args, that.args) &&
                Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, returnType, args, statements, functionExecutor, isFuncExec);
    }

    public static class Builder {
        private final String name;
        private Type returnType;
        private List<FormalArgument> args;
        private FunctionExecutor functionExecutor;
        private List<PMLStatement> body;

        public Builder(String name) {
            this.name = name;
            this.returnType = Type.voidType();
            this.args = new ArrayList<>();
            this.body = new ArrayList<>();
        }

        public Builder returns(Type returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder args(FormalArgument ... args) {
            this.args = new ArrayList<>(List.of(args));
            return this;
        }

        public Builder args(List<FormalArgument> args) {
            this.args = args;
            return this;
        }

        public Builder executor(FunctionExecutor executor) {
            this.functionExecutor = executor;
            return this;
        }

        public Builder body(PMLStatement ... body) {
            this.body = new ArrayList<>(List.of(body));
            return this;
        }

        public Builder body(List<PMLStatement> body) {
            this.body = body;
            return this;
        }

        public FunctionDefinitionStatement build() {
            if (functionExecutor != null) {
                return new FunctionDefinitionStatement(name, returnType, args, functionExecutor);
            }

            return new FunctionDefinitionStatement(name, returnType, args, body);
        }
    }
}
