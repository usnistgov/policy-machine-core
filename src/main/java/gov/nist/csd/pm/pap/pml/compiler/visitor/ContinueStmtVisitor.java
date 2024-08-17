package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.statement.ContinueStatement;
import org.antlr.v4.runtime.ParserRuleContext;

public class ContinueStmtVisitor extends PMLBaseVisitor<ContinueStatement> {

    public ContinueStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public ContinueStatement visitContinueStatement(PMLParser.ContinueStatementContext ctx) {
        // check that there is a for loop descendant
        if (!inFor(ctx)) {
            throw new PMLCompilationRuntimeException(
                    ctx,
                    "continue statement not in foreach"
            );
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
