package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.pml.statement.ErrorStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStatement;

import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class FunctionReturnStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public FunctionReturnStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitReturnStatement(PMLParser.ReturnStatementContext ctx) {
        // check that the return statement is inside a function
        if (!inFunctionOrResponse(ctx)) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "return statement not in function definition or obligation response"
            );

            return new ErrorStatement(ctx);
        }

        if (ctx.expression() == null) {
            return new FunctionReturnStatement();
        }

        Expression e = Expression.compile(visitorCtx, ctx.expression(), Type.any());

        return new FunctionReturnStatement(e);
    }

    private boolean inFunctionOrResponse(ParserRuleContext ctx) {
        if (ctx instanceof PMLParser.FunctionDefinitionStatementContext || ctx instanceof PMLParser.ResponseContext) {
            return true;
        } else if (ctx == null) {
            return false;
        }

        return inFunctionOrResponse(ctx.getParent());
    }
}
