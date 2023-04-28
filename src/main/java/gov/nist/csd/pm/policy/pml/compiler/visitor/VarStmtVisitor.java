package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.VarStatement;

public class VarStmtVisitor extends PMLBaseVisitor<VarStatement> {

    private final VisitorContext visitorCtx;

    public VarStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public VarStatement visitVariableDeclarationStatement(PMLParser.VariableDeclarationStatementContext ctx) {
        String varName = ctx.ID().getText();
        PMLParser.ExpressionContext expressionCtx = ctx.expression();
        Expression expr = Expression.compile(visitorCtx, expressionCtx);
        boolean isConst = ctx.CONST() != null;

        VarStatement varStatement = new VarStatement(varName, expr, isConst);

        try {
            if (ctx.CONST() == null && ctx.LET() == null
                    && (visitorCtx.scope().variableExists(varName) && visitorCtx.scope().getVariable(varName).isConst())) {
                    visitorCtx.errorLog().addError(
                            ctx,
                            "cannot reassign const variable"
                    );
                    return varStatement;
            }

            visitorCtx.scope().addVariable(varName, expr.getType(visitorCtx.scope()), isConst);
        } catch (PMLScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        return varStatement;
    }
}
