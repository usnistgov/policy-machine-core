package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStmt;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import org.antlr.v4.runtime.ParserRuleContext;

public class FunctionReturnStmtVisitor extends PMLBaseVisitor<FunctionReturnStmt> {

    private final VisitorContext visitorCtx;

    public FunctionReturnStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public FunctionReturnStmt visitFunctionReturnStatement(PMLParser.FunctionReturnStatementContext ctx) {
        // check that the return statement is inside a function
        if (!inFunction(ctx)) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "return statement not in function definition"
            );
        }

        if (ctx.expression() == null) {
            return new FunctionReturnStmt();
        } else {
            Expression expr = Expression.compile(visitorCtx, ctx.expression());

            return new FunctionReturnStmt(expr);
        }
    }

    private boolean inFunction(ParserRuleContext ctx) {
        if (ctx instanceof PMLParser.FunctionDefinitionStatementContext) {
            return true;
        } else if (ctx == null) {
            return false;
        }

        return inFunction(ctx.getParent());
    }
}
