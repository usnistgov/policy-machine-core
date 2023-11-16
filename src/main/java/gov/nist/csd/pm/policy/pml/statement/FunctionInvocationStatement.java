package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.function.FunctionExecutor;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.value.ReturnValue;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.List;
import java.util.Objects;

public class FunctionInvocationStatement extends PMLStatement {

    private String functionName;
    private List<Expression> actualArgs;

    public FunctionInvocationStatement(String functionName, List<Expression> actualArgs) {
        this.functionName = functionName;
        this.actualArgs = actualArgs;
    }

    public FunctionInvocationStatement(PMLParser.FunctionInvokeContext funcCallCtx) {
        super(funcCallCtx);
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

        Value value = new VoidValue();
        if (functionDef.isFunctionExecutor()) {
            FunctionExecutor functionExecutor = functionDef.getFunctionExecutor();
            try {
                value = functionExecutor.exec(ctx, policy);
            } catch (PMLScopeException e) {
                throw new PMLExecutionException(e);
            }
        } else {
            List<PMLStatement> statements = functionDef.getBody();
            for (PMLStatement stmt : statements) {
                value = stmt.execute(ctx, policy);
                if (value instanceof ReturnValue) {
                    break;
                }
            }
        }

        ctx.scope().local().overwriteFromLocalScope(ctx.scope().local());

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
