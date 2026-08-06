package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.ContinueStatement;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Compiles a PML continue statement into a {@link ContinueStatement}, rejecting it if it does not
 * appear inside a foreach loop.
 */
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
