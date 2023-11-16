package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.BreakStatement;
import org.antlr.v4.runtime.ParserRuleContext;

public class BreakStmtVisitor extends PMLBaseVisitor<BreakStatement> {

    public BreakStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public BreakStatement visitBreakStatement(PMLParser.BreakStatementContext ctx) {
        // check that there is a for loop parent
        if (!inFor(ctx)) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "break statement not in foreach"
            );

            return new BreakStatement(ctx);
        }

        return new BreakStatement();
    }

    private boolean inFor(ParserRuleContext ctx) {
        if (ctx instanceof PMLParser.ForeachStatementContext) {
            return true;
        } else if (ctx == null) {
            return false;
        }

        return inFor(ctx.getParent());
    }
}