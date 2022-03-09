package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;

import java.util.List;

public class PolicyVisitor extends PALBaseVisitor<List<PALStatement>> {

    private final VisitorContext visitorCtx;

    public PolicyVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public List<PALStatement> visitPal(PALParser.PalContext ctx) {
        StatementsVisitor visitor = new StatementsVisitor(visitorCtx);
        return visitor.visitStmts(ctx.stmts());
    }

}
