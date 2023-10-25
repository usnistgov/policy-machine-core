package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.ContinueStatement;
import gov.nist.csd.pm.policy.pml.statement.ErrorStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.antlr.v4.runtime.ParserRuleContext;

public class ContinueStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public ContinueStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitContinueStatement(PMLParser.ContinueStatementContext ctx) {
        // check that there is a for loop parent
        if (!inFor(ctx)) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "continue statement not in foreach"
            );

            return new ErrorStatement(ctx);
        }

        return new ContinueStatement();
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
