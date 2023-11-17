package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.expression.FunctionInvokeExpression;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class FunctionInvokeStmtVisitor extends PMLBaseVisitor<FunctionInvokeExpression> {

    public FunctionInvokeStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public FunctionInvokeExpression visitFunctionInvoke(PMLParser.FunctionInvokeContext ctx) {
        return parse(ctx);
    }

    @Override
    public FunctionInvokeExpression visitFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx) {
        return parse(ctx.functionInvoke());
    }

    private FunctionInvokeExpression parse(PMLParser.FunctionInvokeContext funcCallCtx) {
        String funcName = funcCallCtx.ID().getText();

        // get actual arg expressions
        PMLParser.FunctionInvokeArgsContext funcCallArgsCtx = funcCallCtx.functionInvokeArgs();
        List<Expression> actualArgs = new ArrayList<>();

        PMLParser.ExpressionListContext expressionListContext = funcCallArgsCtx.expressionList();
        if (expressionListContext != null) {
            for (PMLParser.ExpressionContext exprCtx : expressionListContext.expression()) {
                Expression expr = Expression.compile(visitorCtx, exprCtx, Type.any());

                actualArgs.add(expr);
            }
        }

        // check the function is in scope and the args are correct
        FunctionSignature functionSignature;
        try {
            functionSignature = visitorCtx.scope().getFunction(funcName);
        } catch (UnknownFunctionInScopeException e) {
            visitorCtx.errorLog().addError(funcCallCtx, e.getMessage());

            return new FunctionInvokeExpression(funcCallCtx);
        }

        // check that the actual args are correct type
        List<FormalArgument> formalArgs = functionSignature.getArgs();

        if (formalArgs.size() != actualArgs.size()) {
            visitorCtx.errorLog().addError(
                    funcCallCtx,
                    "wrong number of args for function call " + funcName + ": " +
                            "expected " + formalArgs.size() + ", got " + actualArgs.size()
            );

            return new FunctionInvokeExpression(funcCallCtx);
        } else {
            for (int i = 0; i < actualArgs.size(); i++) {
                try {
                    Expression actual = actualArgs.get(i);
                    Type actualType = actual.getType(visitorCtx.scope());
                    FormalArgument formal = formalArgs.get(i);

                    if (!actual.getType(visitorCtx.scope()).equals(formal.type())) {
                        visitorCtx.errorLog().addError(
                                funcCallCtx,
                                "invalid argument type: expected " + formal.type() + ", got " +
                                        actualType + " at arg " + i
                        );

                        return new FunctionInvokeExpression(funcCallCtx);
                    }
                } catch (PMLScopeException e) {
                    visitorCtx.errorLog().addError(funcCallCtx, e.getMessage());

                    return new FunctionInvokeExpression(funcCallCtx);
                }
            }
        }

        return new FunctionInvokeExpression(funcName, functionSignature.getReturnType(), actualArgs);
    }
}