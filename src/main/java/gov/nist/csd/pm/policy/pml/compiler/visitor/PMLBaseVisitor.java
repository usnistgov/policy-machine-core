package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;

public abstract class PMLBaseVisitor<S> extends PMLParserBaseVisitor<S> {

    protected VisitorContext visitorCtx;

    public PMLBaseVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }
}
