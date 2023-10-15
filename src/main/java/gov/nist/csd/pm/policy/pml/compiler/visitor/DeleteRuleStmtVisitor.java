package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.DeleteRuleStatement;


public class DeleteRuleStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public DeleteRuleStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitDeleteRuleStatement(PMLParser.DeleteRuleStatementContext ctx) {
        Expression ruleNameExpr = Expression.compile(visitorCtx, ctx.ruleName, Type.string());
        Expression oblNameExpr = Expression.compile(visitorCtx, ctx.obligationName, Type.string());
        return new DeleteRuleStatement(ruleNameExpr, oblNameExpr);
    }
}
