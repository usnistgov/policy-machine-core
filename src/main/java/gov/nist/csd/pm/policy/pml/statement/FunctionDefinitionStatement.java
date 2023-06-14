package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.model.function.FunctionExecutor;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.PMLFormatter;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;

public class FunctionDefinitionStatement extends PMLStatement {

    public static String name(String name) {
        return name;
    }

    public static Type returns(Type type) {
        return type;
    }

    public static List<FormalArgument> args(FormalArgument ... formalArguments) {
        return Arrays.stream(formalArguments).toList();
    }

    private final String functionName;
    private final Type returnType;
    private final List<FormalArgument> args;
    private List<PMLStatement> statements;
    private FunctionExecutor functionExecutor;
    private boolean isFuncExec;

    public FunctionDefinitionStatement(FunctionDefinitionStatement functionDefinitionStatement) {
        this.functionName = functionDefinitionStatement.functionName;
        this.returnType = functionDefinitionStatement.returnType;
        this.args = functionDefinitionStatement.args;
        this.statements = functionDefinitionStatement.statements;
        this.functionExecutor = functionDefinitionStatement.functionExecutor;
        this.isFuncExec = functionDefinitionStatement.isFuncExec;
    }
    public FunctionDefinitionStatement(String functionName, Type returnType, List<FormalArgument> args, List<PMLStatement> stmts) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.args = args;
        this.statements = stmts;
    }

    public FunctionDefinitionStatement(String functionName, Type returnType,
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

        return new Value();
    }

    @Override
    public String toString() {
        String argsStr = serializeFormalArgs();

        return String.format(
                "function %s(%s) %s {%s}",
                functionName,
                argsStr,
                returnType.toString(),
                PMLFormatter.statementsToString(statements)
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
        }

        public Builder returns(Type type) {
            this.returnType = type;
            return this;
        }

        public Builder args(FormalArgument ... args) {
            this.args = new ArrayList<>(List.of(args));
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

        public FunctionDefinitionStatement build() {
            if (functionExecutor != null) {
                return new FunctionDefinitionStatement(name, returnType, args, functionExecutor);
            }

            return new FunctionDefinitionStatement(name, returnType, args, body);
        }
    }
}
