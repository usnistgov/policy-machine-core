package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionExecutor;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.value.ReturnValue;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

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
        FunctionDefinitionStatement functionDef = ctx.scope().getFunction(functionName);
        ExecutionContext localCtx = ctx.copy();


        List<FormalArgument> formalArgs = functionDef.getArgs();

        if (formalArgs.size() != actualArgs.size()) {
            throw new PMLExecutionException("expected " + formalArgs.size() + " args for function \""
                                                    + functionName + "\", got " + actualArgs.size());
        }

        for (int i = 0; i < actualArgs.size(); i++) {
            Expression argExpr = actualArgs.get(i);
            Value argValue = argExpr.execute(localCtx, policy);
            FormalArgument formalArg = formalArgs.get(i);

            if (!argValue.getType().equals(formalArg.type())) {
                throw new PMLExecutionException("expected " + formalArg.type() + " for arg " + i + " for function \""
                                                        + functionName + "\", got " + argValue.getType());
            }

            localCtx.scope().addValue(formalArg.name(), argValue);
        }

        Value value = new VoidValue();
        if (functionDef.isFunctionExecutor()) {
            FunctionExecutor functionExecutor = functionDef.getFunctionExecutor();
            try {
                value = functionExecutor.exec(localCtx, policy);
            } catch (PMLScopeException e) {
                throw new PMLExecutionException(e);
            }
        } else {
            List<PMLStatement> statements = functionDef.getBody();
            for (PMLStatement stmt : statements) {
                value = stmt.execute(localCtx, policy);
                if (value instanceof ReturnValue) {
                    break;
                }
            }
        }

        ctx.scope().overwriteValues(localCtx.scope());

        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return  String.format("%s(%s)", functionName, argsToString());
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
