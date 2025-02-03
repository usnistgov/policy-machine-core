package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteRuleStatement;
import gov.nist.csd.pm.pap.pml.type.Type;


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
