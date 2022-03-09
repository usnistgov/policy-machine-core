package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.BreakStatement;
import org.antlr.v4.runtime.ParserRuleContext;

public class BreakStmtVisitor extends PALBaseVisitor<BreakStatement> {

    private final VisitorContext visitorCtx;

    public BreakStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public BreakStatement visitBreakStmt(PALParser.BreakStmtContext ctx) {
        // check that there is a for loop parent
        if (!inFor(ctx)) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "continue statement not in foreach"
            );
        }

        return new BreakStatement();
    }

    private boolean inFor(ParserRuleContext ctx) {
        if (ctx instanceof PALParser.ForeachStmtContext) {
            return true;
        } else if (ctx == null) {
            return false;
        }

        return inFor(ctx.getParent());
    }
}