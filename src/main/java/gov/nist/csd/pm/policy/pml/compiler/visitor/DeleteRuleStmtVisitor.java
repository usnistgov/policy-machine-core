package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.DeleteRuleStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;

public class DeleteRuleStmtVisitor extends PMLBaseVisitor<DeleteRuleStatement> {

    private final VisitorContext visitorCtx;

    public DeleteRuleStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DeleteRuleStatement visitDeleteRuleStmt(PMLParser.DeleteRuleStmtContext ctx) {
        Expression ruleNameExpr = Expression.compile(visitorCtx, ctx.ruleName, Type.string());
        Expression oblNameExpr = Expression.compile(visitorCtx, ctx.obligationName, Type.string());
        return new DeleteRuleStatement(ruleNameExpr, oblNameExpr);
    }
}
