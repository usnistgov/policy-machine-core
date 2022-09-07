package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.AssociateStatement;

public class AssociateStmtVisitor extends PALBaseVisitor<AssociateStatement> {

    private final VisitorContext visitorCtx;

    public AssociateStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public AssociateStatement visitAssociateStmt(PALParser.AssociateStmtContext ctx) {
        Expression ua = Expression.compile(visitorCtx, ctx.ua, Type.string());
        Expression target = Expression.compile(visitorCtx, ctx.target, Type.string());
        Expression accessRights = new AccessRightsVisitor().visitAccessRightArray(ctx.accessRights);

        return new AssociateStatement(ua, target, accessRights);
    }
}
