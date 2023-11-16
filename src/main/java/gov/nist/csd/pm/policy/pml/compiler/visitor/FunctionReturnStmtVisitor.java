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
        // check that the return statement is inside a function
        if (!inFunctionOrResponse(ctx)) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "return statement not in function definition or obligation response"
            );

            return new FunctionReturnStatement(ctx);
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
