package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParserBaseVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Base class for PML grammar-context visitors, giving each visitor access to the shared
 * {@link VisitorContext} (symbol scope, error log) for the compilation in progress.
 */
public abstract class PMLBaseVisitor<S> extends PMLParserBaseVisitor<S> {

    protected VisitorContext visitorCtx;

    public PMLBaseVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    /**
     * Records a compile error derived from an exception's message against the given parse context.
     */
    public void logError(ParserRuleContext ctx, Exception e) {
        visitorCtx.errorLog().addError(ctx, e.getMessage());
    }

    /**
     * Records a compile error against the given parse context.
     */
    public void logError(ParserRuleContext ctx, String msg) {
        visitorCtx.errorLog().addError(ctx, msg);
    }
}
