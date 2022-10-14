package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.DeleteRuleStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

public class DeleteRuleStmtVisitor extends PALBaseVisitor<DeleteRuleStatement> {

    private final VisitorContext visitorCtx;

    public DeleteRuleStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DeleteRuleStatement visitDeleteRuleStmt(PALParser.DeleteRuleStmtContext ctx) {
        Expression ruleNameExpr = Expression.compile(visitorCtx, ctx.ruleName, Type.string());
        Expression oblNameExpr = Expression.compile(visitorCtx, ctx.obligationName, Type.string());
        return new DeleteRuleStatement(ruleNameExpr, oblNameExpr);
    }
}
