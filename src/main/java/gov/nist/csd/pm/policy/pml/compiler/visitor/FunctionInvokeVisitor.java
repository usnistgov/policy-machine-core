package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.FunctionInvocationStatement;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;

import java.util.ArrayList;
import java.util.List;

public class FunctionInvokeVisitor extends PMLBaseVisitor<FunctionInvocationStatement> {

    private final VisitorContext visitorCtx;

    public FunctionInvokeVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public FunctionInvocationStatement visitFunctionInvoke(PMLParser.FunctionInvokeContext ctx) {
        return parse(ctx);
    }

    @Override
    public FunctionInvocationStatement visitFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx) {
        return parse(ctx.functionInvoke());
    }

    private FunctionInvocationStatement parse(PMLParser.FunctionInvokeContext funcCallCtx) {
        String funcName = funcCallCtx.ID().getText();

        // get actual arg expressions
        PMLParser.FunctionInvokeArgsContext funcCallArgsCtx = funcCallCtx.functionInvokeArgs();
        List<Expression> actualArgs = new ArrayList<>();
        for (PMLParser.ExpressionContext exprCtx : funcCallArgsCtx.expression()) {
            Expression expr = Expression.compile(visitorCtx, exprCtx);

            actualArgs.add(expr);
        }

        // check the function is in scope and the args are correct
        FunctionDefinitionStatement functionDefinitionStmt = null;
        try {
            functionDefinitionStmt = visitorCtx.scope().getFunction(funcName);
        } catch (UnknownFunctionInScopeException e) {
            visitorCtx.errorLog().addError(funcCallCtx, e.getMessage());
        }

        // if the stmt is null then there was a compilation error, just return the statement
        if (functionDefinitionStmt == null) {
            return new FunctionInvocationStatement(funcName, actualArgs);
        }

        // check that the actual args are correct type
        List<FormalArgument> formalArgs = functionDefinitionStmt.getArgs();

        if (formalArgs.size() != actualArgs.size()) {
            visitorCtx.errorLog().addError(
                    funcCallCtx,
                    "wrong number of args for function call " + funcName + ": " +
                            "expected " + formalArgs.size() + ", got " + actualArgs.size()
            );
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
                    }
                } catch (PMLScopeException e) {
                    visitorCtx.errorLog().addError(funcCallCtx, e.getMessage());
                }
            }
        }

        return new FunctionInvocationStatement(funcName, actualArgs);
    }
}
