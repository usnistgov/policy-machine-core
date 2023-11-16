package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.value.Value;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.Serializable;
import java.util.List;

public abstract class PMLStatement implements Serializable {

    private ParserRuleContext ctx;

    public PMLStatement() {}

    public PMLStatement(ParserRuleContext ctx) {
        this.ctx = ctx;
    }

    public ParserRuleContext getCtx() {
        return ctx;
    }

    public boolean hasError() {
        return ctx != null;
    }

    protected abstract Value execute(ExecutionContext ctx, Policy policy) throws PMException;

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public final String toString() {
        return toFormattedString(0);
    }

    public abstract String toFormattedString(int indentLevel);

    public static String indent(int indentLevel) {
        String INDENT = "    ";
        return INDENT.repeat(indentLevel);
    }

    public static Value execute(ExecutionContext ctx, Policy policy, PMLStatement stmt) throws PMException {
        if (stmt instanceof FunctionInvocationStatement functionInvocationStatement) {
            FunctionDefinitionStatement functionDef = ctx.scope().getFunction(functionInvocationStatement.getFunctionName());
            return executeFunctionInvoke(ctx, policy, functionInvocationStatement, functionDef);
        }

        return stmt.execute(ctx, policy);
    }

    private static Value executeFunctionInvoke(ExecutionContext ctx, Policy policy,
                                               FunctionInvocationStatement funcInvoke,
                                               FunctionDefinitionStatement funcDef) throws PMException {
        String funcName = funcDef.getSignature().getFunctionName();
        List<Expression> actualArgs = funcInvoke.getActualArgs();

        List<FormalArgument> formalArgs = funcDef.getSignature().getArgs();

        if (formalArgs.size() != actualArgs.size()) {
            throw new PMLExecutionException("expected " + formalArgs.size() + " args for function \""
                                                    + funcName + "\", got " + actualArgs.size());
        }

        ExecutionContext funcInvokeExecCtx = ctx.copy();

        for (int i = 0; i < actualArgs.size(); i++) {
            Expression argExpr = actualArgs.get(i);
            Value argValue = argExpr.execute(funcInvokeExecCtx, policy);
            FormalArgument formalArg = formalArgs.get(i);

            if (!argValue.getType().equals(formalArg.type())) {
                throw new PMLExecutionException("expected " + formalArg.type() + " for arg " + i + " for function \""
                                                        + funcName + "\", got " + argValue.getType());
            }

            funcInvokeExecCtx.scope().local().addOrOverwriteVariable(formalArg.name(), argValue);
        }

        return funcInvoke.execute(funcInvokeExecCtx, policy);
    }
}
