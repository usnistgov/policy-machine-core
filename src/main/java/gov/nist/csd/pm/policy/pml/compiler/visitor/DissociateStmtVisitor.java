package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.DissociateStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;

public class DissociateStmtVisitor extends PMLBaseVisitor<DissociateStatement> {

    private final VisitorContext visitorCtx;

    public DissociateStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DissociateStatement visitDissociateStatement(PMLParser.DissociateStatementContext ctx) {
        Expression ua = Expression.compile(visitorCtx, ctx.ua, Type.string());
        Expression target = Expression.compile(visitorCtx, ctx.target, Type.string());
        return new DissociateStatement(ua, target);
    }
}
