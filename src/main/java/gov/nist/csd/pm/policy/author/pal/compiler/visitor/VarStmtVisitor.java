package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.scope.PALScopeException;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.author.pal.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.VarStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class VarStmtVisitor extends PALBaseVisitor<VarStatement> {

    private final VisitorContext visitorCtx;

    public VarStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public VarStatement visitVarStmt(PALParser.VarStmtContext ctx) {
        String varName = ctx.id().getText();
        PALParser.ExpressionContext expressionCtx = ctx.expression();
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
        } catch (PALScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        return varStatement;
    }
}
