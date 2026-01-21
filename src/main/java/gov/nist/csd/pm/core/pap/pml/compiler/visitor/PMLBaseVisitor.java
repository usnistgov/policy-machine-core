package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class PMLBaseVisitor<S> extends PMLParserBaseVisitor<S> {

    protected VisitorContext visitorCtx;

    public PMLBaseVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    public void logError(ParserRuleContext ctx, Exception e) {
        visitorCtx.errorLog().addError(ctx, e.getMessage());
    }

    public void logError(ParserRuleContext ctx, String msg) {
        visitorCtx.errorLog().addError(ctx, msg);
    }
}
