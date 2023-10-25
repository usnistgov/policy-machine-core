package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.pml.statement.ErrorStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionInvocationStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class FunctionInvokeStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public FunctionInvokeStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitFunctionInvoke(PMLParser.FunctionInvokeContext ctx) {
        return parse(ctx);
    }

    @Override
    public PMLStatement visitFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx) {
        return parse(ctx.functionInvoke());
    }

    private PMLStatement parse(PMLParser.FunctionInvokeContext funcCallCtx) {
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
        FunctionDefinitionStatement functionDefinitionStmt;
        try {
            functionDefinitionStmt = visitorCtx.scope().getFunction(funcName);
        } catch (UnknownFunctionInScopeException e) {
            visitorCtx.errorLog().addError(funcCallCtx, e.getMessage());

            return new ErrorStatement(funcCallCtx);
        }

        // check that the actual args are correct type
        List<FormalArgument> formalArgs = functionDefinitionStmt.getArgs();

        if (formalArgs.size() != actualArgs.size()) {
            visitorCtx.errorLog().addError(
                    funcCallCtx,
                    "wrong number of args for function call " + funcName + ": " +
                            "expected " + formalArgs.size() + ", got " + actualArgs.size()
            );

            return new ErrorStatement(funcCallCtx);
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

                        return new ErrorStatement(funcCallCtx);
                    }
                } catch (PMLScopeException e) {
                    visitorCtx.errorLog().addError(funcCallCtx, e.getMessage());

                    return new ErrorStatement(funcCallCtx);
                }
            }
        }

        return new FunctionInvocationStatement(funcName, actualArgs);
    }
}