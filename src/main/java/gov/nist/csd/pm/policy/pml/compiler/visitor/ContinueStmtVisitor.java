package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.ContinueStatement;
import org.antlr.v4.runtime.ParserRuleContext;

public class ContinueStmtVisitor extends PMLBaseVisitor<ContinueStatement> {

    private final VisitorContext visitorCtx;

    public ContinueStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public ContinueStatement visitContinueStmt(PMLParser.ContinueStmtContext ctx) {
        // check that there is a for loop parent
        if (!inFor(ctx)) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "continue statement not in foreach"
            );
        }

        return new ContinueStatement();
    }

    private boolean inFor(ParserRuleContext ctx) {
        if (ctx instanceof PMLParser.ForeachStmtContext) {
            return true;
        } else if (ctx == null) {
            return false;
        }

        return inFor(ctx.getParent());
    }
}
