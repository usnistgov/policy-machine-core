package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FunctionExecutor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.model.scope.PALScopeException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.List;
import java.util.Objects;

public class FunctionStatement extends PALStatement {

    private final String functionName;
    private final List<Expression> actualArgs;

    public FunctionStatement(String functionName, List<Expression> actualArgs) {
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
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        try {
            FunctionDefinitionStatement functionDef = ctx.scope().getFunction(functionName);
            ExecutionContext localCtx = ctx.copy();

            List<FormalArgument> formalArgs = functionDef.getArgs();

            for (int i = 0; i < actualArgs.size(); i++) {
                Expression argExpr = actualArgs.get(i);
                Value argValue = argExpr.execute(localCtx, policyAuthor);
                FormalArgument formalArg = formalArgs.get(i);

                if (!argValue.getType().equals(formalArg.type())) {
                    throw new PMException("actual arg value has type " + argValue.getType() + " expected " + formalArg.type());
                }

                localCtx.scope().addValue(formalArg.name(), argValue);
            }

            Value value = new Value();
            if (functionDef.isFunctionExecutor()) {
                FunctionExecutor functionExecutor = functionDef.getFunctionExecutor();
                value = functionExecutor.exec(localCtx, policyAuthor);
            } else {
                List<PALStatement> statements = functionDef.getBody();
                for (PALStatement stmt : statements) {
                    value = stmt.execute(localCtx, policyAuthor);
                    if (value.isReturn()) {
                        break;
                    }
                }
            }

            ctx.scope().overwriteValues(localCtx.scope());

            return value;
        } catch (PALScopeException e) {
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
        FunctionStatement that = (FunctionStatement) o;
        return Objects.equals(functionName, that.functionName)
                && Objects.equals(actualArgs, that.actualArgs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, actualArgs);
    }

}
