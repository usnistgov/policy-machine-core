package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.DeleteRuleStatement;
import gov.nist.csd.pm.policy.pml.type.Type;


public class DeleteRuleStmtVisitor extends PMLBaseVisitor<DeleteRuleStatement> {

    public DeleteRuleStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeleteRuleStatement visitDeleteRuleStatement(PMLParser.DeleteRuleStatementContext ctx) {
        Expression ruleNameExpr = Expression.compile(visitorCtx, ctx.ruleName, Type.string());
        Expression oblNameExpr = Expression.compile(visitorCtx, ctx.obligationName, Type.string());
        return new DeleteRuleStatement(ruleNameExpr, oblNameExpr);
    }
}
