package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMLFunctionNotDefinedException;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionExecutor;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
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

    private final FunctionSignature signature;
    private List<PMLStatement> statements;
    private FunctionExecutor functionExecutor;
    private boolean isFuncExec;

    public FunctionDefinitionStatement(FunctionDefinitionStatement functionDefinitionStatement) {
        this.signature = functionDefinitionStatement.signature;
        this.statements = functionDefinitionStatement.statements;
        this.functionExecutor = functionDefinitionStatement.functionExecutor;
        this.isFuncExec = functionDefinitionStatement.isFuncExec;
    }
    private FunctionDefinitionStatement(String functionName, Type returnType, List<FormalArgument> args, List<PMLStatement> stmts) {
        this.signature = new FunctionSignature(functionName, returnType, args);
        this.statements = stmts;
    }

    private FunctionDefinitionStatement(String functionName, Type returnType,
                                       List<FormalArgument> args, FunctionExecutor executor) {
        this.signature = new FunctionSignature(functionName, returnType, args);
        this.functionExecutor = executor;
        this.isFuncExec = true;
    }

    public boolean isFunctionExecutor() {
        return isFuncExec;
    }

    public FunctionExecutor getFunctionExecutor() {
        return functionExecutor;
    }

    public FunctionSignature signature() {
        return signature;
    }

    public List<PMLStatement> getBody() {
        return statements;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMLExecutionException {
        try {
            ctx.scope().addFunctionSignature(this.signature);
            ctx.scope().addFunction(this);
        } catch (FunctionAlreadyDefinedInScopeException | PMLFunctionNotDefinedException e) {
            throw new PMLExecutionException(e.getMessage());
        }

        return new VoidValue();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        if (isFuncExec) {
            throw new RuntimeException("PML Function " + signature.getFunctionName() + " cannot be serialized as it is a Java function");
        }

        return String.format(
                "%s%s",
                signature.toFormattedString(indentLevel),
                new PMLStatementBlock(statements).toFormattedString(indentLevel)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FunctionDefinitionStatement that = (FunctionDefinitionStatement) o;
        return isFuncExec == that.isFuncExec && Objects.equals(
                signature, that.signature) && Objects.equals(
                statements, that.statements) && Objects.equals(
                functionExecutor, that.functionExecutor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signature, statements, functionExecutor, isFuncExec);
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
