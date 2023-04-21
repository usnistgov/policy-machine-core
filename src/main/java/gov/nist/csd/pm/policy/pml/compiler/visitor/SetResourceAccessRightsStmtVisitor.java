package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.SetResourceAccessRightsStatement;

public class SetResourceAccessRightsStmtVisitor extends PMLBaseVisitor<SetResourceAccessRightsStatement> {

    private final VisitorContext visitorCtx;

    public SetResourceAccessRightsStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public SetResourceAccessRightsStatement visitSetResourceAccessRightsStmt(PMLParser.SetResourceAccessRightsStmtContext ctx) {
        if (visitorCtx.scope().isResourceAccessRightsExpressionSet()) {
            visitorCtx.errorLog().addError(ctx, "set resource access rights has already been called");
            return new SetResourceAccessRightsStatement(visitorCtx.scope().getResourceAccessRightsExpression());
        }

        Expression exprList = Expression.compile(visitorCtx, ctx.accessRights, Type.array(Type.string()));

        visitorCtx.scope().setResourceAccessRightsExpression(exprList);

        return new SetResourceAccessRightsStatement(exprList);
    }
}
