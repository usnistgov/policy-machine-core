package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.AssociateStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

public class AssociateStmtVisitor extends PMLBaseVisitor<AssociateStatement> {

    public AssociateStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public AssociateStatement visitAssociateStatement(PMLParser.AssociateStatementContext ctx) {
        Expression ua = Expression.compile(visitorCtx, ctx.ua, Type.string());
        Expression target = Expression.compile(visitorCtx, ctx.target, Type.string());
        Expression accessRights = Expression.compile(visitorCtx, ctx.accessRights, Type.array(Type.string()));

        return new AssociateStatement(ua, target, accessRights);
    }
}
