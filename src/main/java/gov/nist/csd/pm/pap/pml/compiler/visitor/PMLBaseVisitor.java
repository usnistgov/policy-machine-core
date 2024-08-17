package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;

public abstract class PMLBaseVisitor<S> extends PMLParserBaseVisitor<S> {

    protected VisitorContext visitorCtx;

    public PMLBaseVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }
}
