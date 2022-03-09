package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.function.FunctionReturnStmt;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import org.antlr.v4.runtime.ParserRuleContext;

public class FunctionReturnStmtVisitor extends PALBaseVisitor<FunctionReturnStmt> {

    private final VisitorContext visitorCtx;

    public FunctionReturnStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public FunctionReturnStmt visitFuncReturnStmt(PALParser.FuncReturnStmtContext ctx) {
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
        if (ctx instanceof PALParser.FuncDefStmtContext) {
            return true;
        } else if (ctx == null) {
            return false;
        }

        return inFunction(ctx.getParent());
    }
}
