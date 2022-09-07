package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.VarStatement;

public class VarStmtVisitor extends PALBaseVisitor<VarStatement> {

    private final VisitorContext visitorCtx;

    public VarStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public VarStatement visitVarStmt(PALParser.VarStmtContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        PALParser.ExpressionContext expressionCtx = ctx.expression();
        Expression expr = Expression.compile(visitorCtx, expressionCtx);
        boolean isConst = ctx.CONST() != null;

        if (ctx.LET() != null) {
            if (visitorCtx.scope().hasVariable(varName)) {
                visitorCtx.errorLog().addError(
                        ctx,
                        "variable with name " + varName + " already exists"
                );
            }
        } else if (ctx.CONST() != null) {
            if (visitorCtx.scope().hasVariable(varName)) {
                visitorCtx.errorLog().addError(
                        ctx,
                        "const with name " + varName + " already exists"
                );
            }
        } else {
            if (visitorCtx.scope().hasVariable(varName) && visitorCtx.scope().getVariable(varName).isConst()) {
                visitorCtx.errorLog().addError(
                        ctx,
                        "cannot reassign const variable"
                );
            }
        }

        visitorCtx.scope().addVariable(varName, expr.getType(visitorCtx.scope()), isConst);

        return new VarStatement(varName, expr, isConst);
    }
}
