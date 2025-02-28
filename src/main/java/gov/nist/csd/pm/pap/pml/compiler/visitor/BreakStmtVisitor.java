package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.statement.basic.BreakStatement;
import org.antlr.v4.runtime.ParserRuleContext;

public class BreakStmtVisitor extends PMLBaseVisitor<BreakStatement> {

    public BreakStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public BreakStatement visitBreakStatement(PMLParser.BreakStatementContext ctx) {
        // check that there is a for loop descendant
        if (!inFor(ctx)) {
            throw new PMLCompilationRuntimeException(ctx, "break statement not in foreach");
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