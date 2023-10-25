package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.ErrorStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.statement.SetResourceAccessRightsStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

public class SetResourceAccessRightsStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public SetResourceAccessRightsStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitSetResourceAccessRightsStatement(PMLParser.SetResourceAccessRightsStatementContext ctx) {
        if (visitorCtx.scope().isResourceAccessRightsExpressionSet()) {
            visitorCtx.errorLog().addError(ctx, "set resource access rights has already been called");
            return new ErrorStatement(ctx);
        }

        Expression exprList = Expression.compile(visitorCtx, ctx.accessRights, Type.array(Type.string()));

        visitorCtx.scope().setResourceAccessRightsExpression(exprList);

        return new SetResourceAccessRightsStatement(exprList);
    }
}
