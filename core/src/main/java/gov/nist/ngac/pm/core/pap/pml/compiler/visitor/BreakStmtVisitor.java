package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.BreakStatement;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Compiles a PML break statement into a {@link BreakStatement}, rejecting it if it does not appear
 * inside a foreach loop.
 */
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