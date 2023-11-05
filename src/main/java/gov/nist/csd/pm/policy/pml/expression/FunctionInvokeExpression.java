package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionExecutor;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunctionInvokeExpression extends Expression {

    public static Expression compileFunctionInvokeExpression(VisitorContext visitorCtx,
                                                             PMLParser.FunctionInvokeExpressionContext functionInvokeExpressionContext) {
        PMLParser.FunctionInvokeContext functionInvokeContext = functionInvokeExpressionContext.functionInvoke();
        String funcName = functionInvokeContext.ID().getText();

        FunctionSignature functionSignature;
        try {
            functionSignature = visitorCtx.scope().getFunctionSignature(funcName);
        } catch (UnknownFunctionInScopeException e) {
            visitorCtx.errorLog().addError(functionInvokeContext, e.getMessage());

            return new ErrorExpression(functionInvokeExpressionContext);
        }

        List<FormalArgument> formalArgs = functionSignature.getArgs();
        PMLParser.FunctionInvokeArgsContext funcCallArgsCtx = functionInvokeContext.functionInvokeArgs();
        List<PMLParser.ExpressionContext> argExpressions =  new ArrayList<>();
        PMLParser.ExpressionListContext expressionListContext = funcCallArgsCtx.expressionList();
        if (expressionListContext != null) {
            argExpressions = expressionListContext.expression();
        }

        if (formalArgs.size() != argExpressions.size()) {
            visitorCtx.errorLog().addError(
                    functionInvokeContext,
                    "wrong number of args for function call " + funcName + ": " +
                            "expected " + formalArgs.size() + ", got " + argExpressions.size()
            );

            return new ErrorExpression(functionInvokeExpressionContext);
        }

        List<Expression> actualArgs = new ArrayList<>();
        for (int i = 0; i < argExpressions.size(); i++) {
            PMLParser.ExpressionContext exprCtx = argExpressions.get(i);
            FormalArgument formalArgument = formalArgs.get(i);

            Expression expr = Expression.compile(visitorCtx, exprCtx, formalArgument.type());
            if (expr instanceof ErrorExpression) {
                return expr;
            }

            actualArgs.add(expr);
        }

        // get result types
        Type returnType = functionSignature.getReturnType();

        return new FunctionInvokeExpression(funcName, returnType, actualArgs);
    }

    private final String functionName;
    private Type result;
    private final List<Expression> actualArgs;

    public FunctionInvokeExpression(String functionName, Type result, List<Expression> actualArgs) {
        this.functionName = functionName;
        this.result = result;
        this.actualArgs = actualArgs;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Type getResult() {
        return result;
    }

    public List<Expression> getActualArgs() {
        return actualArgs;
    }


    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return result;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        FunctionDefinitionStatement functionDef = ctx.scope().getFunction(functionName);
        ExecutionContext localCtx = ctx.copy();

        List<FormalArgument> formalArgs = functionDef.signature().getArgs();

        for (int i = 0; i < actualArgs.size(); i++) {
            Expression argExpr = actualArgs.get(i);
            Value argValue = argExpr.execute(localCtx, policy);
            FormalArgument formalArg = formalArgs.get(i);

            if (!argValue.getType().equals(formalArg.type())) {
                throw new PMException("actual arg value has type " + argValue.getType() + " expected " + formalArg.type());
            }

            localCtx.scope().addValue(formalArg.name(), argValue);
        }

        if (functionDef.isFunctionExecutor()) {
            FunctionExecutor functionExecutor = functionDef.getFunctionExecutor();
            Value ret = functionExecutor.exec(localCtx, policy);

            ctx.scope().overwriteValues(localCtx.scope());

            return ret;
        } else {
            List<PMLStatement> statements = functionDef.getBody();
            for (PMLStatement stmt : statements) {
                Value value = stmt.execute(localCtx, policy);

                if (stmt instanceof FunctionReturnStatement) {
                    ctx.scope().overwriteValues(localCtx.scope());

                    return value;
                }
            }
        }

        ctx.scope().overwriteValues(localCtx.scope());

        // if execution gets here than the function did not have a return statement (void)
        return new VoidValue();

    }

    @Override
    public String toFormattedString(int indentLevel) {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FunctionInvokeExpression that = (FunctionInvokeExpression) o;
        return Objects.equals(functionName, that.functionName) && Objects.equals(
                result, that.result) && Objects.equals(actualArgs, that.actualArgs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, result, actualArgs);
    }
}
