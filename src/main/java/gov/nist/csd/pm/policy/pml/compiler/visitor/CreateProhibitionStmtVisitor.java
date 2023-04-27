package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.statement.CreateProhibitionStatement;

import java.util.ArrayList;
import java.util.List;

public class CreateProhibitionStmtVisitor extends PMLBaseVisitor<CreateProhibitionStatement> {

    private final VisitorContext visitorCtx;

    public CreateProhibitionStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateProhibitionStatement visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
        Expression label = Expression.compile(visitorCtx, ctx.name, Type.string());
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

        List<CreateProhibitionStatement.Container> containers = new ArrayList<>();
        for (PMLParser.ProhibitionContainerExpressionContext contExprCtx : ctx.containers.prohibitionContainerExpression()) {
            boolean isComplement =
                    contExprCtx.IS_COMPLEMENT() != null && contExprCtx.IS_COMPLEMENT().getText().equals("!");
            Expression name = Expression.compile(visitorCtx, contExprCtx.container, Type.string());
            containers.add(new CreateProhibitionStatement.Container(isComplement, name));
        }

        return new CreateProhibitionStatement(label, subject, type, accessRights, isIntersection, containers);
    }
}
