package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.CreateProhibitionStatement;

import java.util.ArrayList;
import java.util.List;

public class CreateProhibitionStmtVisitor extends PALBaseVisitor<CreateProhibitionStatement> {

    private final VisitorContext visitorCtx;

    public CreateProhibitionStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateProhibitionStatement visitCreateProhibitionStmt(PALParser.CreateProhibitionStmtContext ctx) {
        Expression label = Expression.compile(visitorCtx, ctx.subject, Type.string());
        Expression subject = Expression.compile(visitorCtx, ctx.subject, Type.string());
        ProhibitionSubject.Type type;
        if (ctx.USER() != null) {
            type = ProhibitionSubject.Type.USER;
        } else if (ctx.USER_ATTRIBUTE() != null) {
            type = ProhibitionSubject.Type.USER_ATTRIBUTE;
        } else {
            type = ProhibitionSubject.Type.PROCESS;
        }

        Expression accessRights = new AccessRightsVisitor().visitAccessRightArray(ctx.accessRights);

        boolean isIntersection = ctx.INTERSECTION() != null;

        List<CreateProhibitionStatement.Container> containers = new ArrayList<>();
        for (PALParser.ProhibitionContainerExpressionContext contExprCtx : ctx.containers.prohibitionContainerExpression()) {
            boolean isComplement =
                    contExprCtx.IS_COMPLEMENT() != null && contExprCtx.IS_COMPLEMENT().getText().equals("!");
            Expression expr = Expression.compile(visitorCtx, contExprCtx.container, Type.string());
            containers.add(new CreateProhibitionStatement.Container(isComplement, expr));
        }

        return new CreateProhibitionStatement(label, subject, type, accessRights, isIntersection, containers);
    }
}
