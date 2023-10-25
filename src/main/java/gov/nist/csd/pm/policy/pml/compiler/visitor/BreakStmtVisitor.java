package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.BreakStatement;
import gov.nist.csd.pm.policy.pml.statement.ErrorStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.antlr.v4.runtime.ParserRuleContext;

public class BreakStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public BreakStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitBreakStatement(PMLParser.BreakStatementContext ctx) {
        // check that there is a for loop parent
        if (!inFor(ctx)) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "break statement not in foreach"
            );

            return new ErrorStatement(ctx);
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