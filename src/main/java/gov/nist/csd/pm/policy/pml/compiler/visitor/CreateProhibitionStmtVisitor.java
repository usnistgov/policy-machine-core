package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.CreateProhibitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

public class CreateProhibitionStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public CreateProhibitionStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
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
