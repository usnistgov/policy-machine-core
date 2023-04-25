package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FunctionExecutor;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;
import java.util.Objects;

public class FunctionInvocationStatement extends PMLStatement {

    private final String functionName;
    private final List<Expression> actualArgs;

    public FunctionInvocationStatement(String functionName, List<Expression> actualArgs) {
        this.functionName = functionName;
        this.actualArgs = actualArgs;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Expression> getActualArgs() {
        return actualArgs;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        try {
            FunctionDefinitionStatement functionDef = ctx.scope().getFunction(functionName);
            ExecutionContext localCtx = ctx.copy();

            List<FormalArgument> formalArgs = functionDef.getArgs();

            for (int i = 0; i < actualArgs.size(); i++) {
                Expression argExpr = actualArgs.get(i);
                Value argValue = argExpr.execute(localCtx, policy);
                FormalArgument formalArg = formalArgs.get(i);

                if (!argValue.getType().equals(formalArg.type())) {
                    throw new PMException("actual arg value has type " + argValue.getType() + " expected " + formalArg.type());
                }

                localCtx.scope().putValue(formalArg.name(), argValue);
            }

            Value value = new Value();
            if (functionDef.isFunctionExecutor()) {
                FunctionExecutor functionExecutor = functionDef.getFunctionExecutor();
                value = functionExecutor.exec(localCtx, policy);
            } else {
                List<PMLStatement> statements = functionDef.getBody();
                for (PMLStatement stmt : statements) {
                    value = stmt.execute(localCtx, policy);
                    if (value.isReturn()) {
                        break;
                    }
                }
            }

            ctx.scope().overwriteValues(localCtx.scope());

            return value;
        } catch (PMLScopeException e) {
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", functionName, argsToString());
    }

    private String argsToString() {
        StringBuilder s = new StringBuilder();
        for (Expression arg : actualArgs) {
            if (s.length() > 0) {
                s.append(", ");
            }
            s.append(arg);
        }

        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionInvocationStatement that = (FunctionInvocationStatement) o;
        return Objects.equals(functionName, that.functionName)
                && Objects.equals(actualArgs, that.actualArgs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, actualArgs);
    }

}
