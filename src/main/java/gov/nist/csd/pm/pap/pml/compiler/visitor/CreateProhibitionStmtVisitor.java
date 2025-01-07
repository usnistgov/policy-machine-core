package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateProhibitionStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

public class CreateProhibitionStmtVisitor extends PMLBaseVisitor<CreateProhibitionStatement> {

    public CreateProhibitionStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateProhibitionStatement visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());
        Expression subject = Expression.compile(visitorCtx, ctx.subject, Type.string());
        ProhibitionSubject.Type type;
        if (ctx.USER() != null) {
            type = ProhibitionSubject.Type.USER;
        } else if (ctx.USER_ATTRIBUTE() != null) {
            type = ProhibitionSubject.Type.USER_ATTRIBUTE;
        } else {
            type = ProhibitionSubject.Type.PROCESS;
        }

        Expression accessRights = Expression.compile(visitorCtx, ctx.accessRights, Type.array(Type.string()));

        boolean isIntersection = ctx.INTERSECTION() != null;

        Expression cc = Expression.compile(visitorCtx, ctx.containers, Type.array(Type.string()));

        return new CreateProhibitionStatement(name, subject, type, accessRights, isIntersection, cc);
    }
}
