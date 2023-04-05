package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.SetResourceAccessRightsStatement;

import java.util.List;

public class SetResourceAccessRightsStmtVisitor extends PALBaseVisitor<SetResourceAccessRightsStatement> {

    private final VisitorContext visitorCtx;

    public SetResourceAccessRightsStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public SetResourceAccessRightsStatement visitSetResourceAccessRightsStmt(PALParser.SetResourceAccessRightsStmtContext ctx) {
        if (visitorCtx.scope().isResourceAccessRightsExpressionSet()) {
            visitorCtx.errorLog().addError(ctx, "set resource access rights has already been called");
            return new SetResourceAccessRightsStatement(visitorCtx.scope().getResourceAccessRightsExpression());
        }

        Expression exprList = Expression.compile(visitorCtx, ctx.accessRights, Type.array(Type.string()));

        visitorCtx.scope().setResourceAccessRightsExpression(exprList);

        return new SetResourceAccessRightsStatement(exprList);
    }
}
