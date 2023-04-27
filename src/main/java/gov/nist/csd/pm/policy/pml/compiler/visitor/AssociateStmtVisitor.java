package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.AssociateStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;

public class AssociateStmtVisitor extends PMLBaseVisitor<AssociateStatement> {

    private final VisitorContext visitorCtx;

    public AssociateStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public AssociateStatement visitAssociateStatement(PMLParser.AssociateStatementContext ctx) {
        Expression ua = Expression.compile(visitorCtx, ctx.ua, Type.string());
        Expression target = Expression.compile(visitorCtx, ctx.target, Type.string());
        Expression accessRights = Expression.compile(visitorCtx, ctx.accessRights, Type.array(Type.string()));

        return new AssociateStatement(ua, target, accessRights);
    }
}
