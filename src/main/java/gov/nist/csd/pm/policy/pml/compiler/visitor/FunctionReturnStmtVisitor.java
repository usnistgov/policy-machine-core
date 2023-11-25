package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.antlr.v4.runtime.ParserRuleContext;

public class FunctionReturnStmtVisitor extends PMLBaseVisitor<FunctionReturnStatement> {

    public FunctionReturnStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public FunctionReturnStatement visitReturnStatement(PMLParser.ReturnStatementContext ctx) {
        ParserRuleContext enclosingCtx = getEnclosingContext(ctx);
        if (enclosingCtx == null) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "return statement not in function definition or obligation response"
            );

            return new FunctionReturnStatement(ctx);
        }

        if (ctx.expression() == null) {
            return new FunctionReturnStatement();
        } else if (enclosingCtx instanceof PMLParser.ResponseContext) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "return statement in response cannot return a value"
            );

            return new FunctionReturnStatement(ctx);
        }

        Expression e = Expression.compile(visitorCtx, ctx.expression(), Type.any());

        return new FunctionReturnStatement(e);
    }

    private ParserRuleContext getEnclosingContext(ParserRuleContext ctx) {
        if (ctx instanceof PMLParser.FunctionDefinitionStatementContext || ctx instanceof PMLParser.ResponseContext) {
            return ctx;
        } else if (ctx == null) {
            return null;
        }

        return getEnclosingContext(ctx.getParent());
    }
}
