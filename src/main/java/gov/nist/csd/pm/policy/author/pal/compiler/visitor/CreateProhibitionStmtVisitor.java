package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;
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
        NameExpression label = NameExpression.compile(visitorCtx, ctx.name);
        NameExpression subject = NameExpression.compile(visitorCtx, ctx.subject);
        ProhibitionSubject.Type type;
        if (ctx.USER() != null) {
            type = ProhibitionSubject.Type.USER;
        } else if (ctx.USER_ATTRIBUTE() != null) {
            type = ProhibitionSubject.Type.USER_ATTRIBUTE;
        } else {
            type = ProhibitionSubject.Type.PROCESS;
        }

        NameExpression accessRights = new AccessRightsVisitor().visitAccessRightArray(ctx.accessRights);

        boolean isIntersection = ctx.INTERSECTION() != null;

        List<CreateProhibitionStatement.Container> containers = new ArrayList<>();
        for (PALParser.ProhibitionContainerExpressionContext contExprCtx : ctx.containers.prohibitionContainerExpression()) {
            boolean isComplement =
                    contExprCtx.IS_COMPLEMENT() != null && contExprCtx.IS_COMPLEMENT().getText().equals("!");
            NameExpression name = NameExpression.compile(visitorCtx, contExprCtx.container);
            containers.add(new CreateProhibitionStatement.Container(isComplement, name));
        }

        return new CreateProhibitionStatement(label, subject, type, accessRights, isIntersection, containers);
    }
}
