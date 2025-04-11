package gov.nist.csd.pm.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteRuleStatement;



public class DeleteRuleStmtVisitor extends PMLBaseVisitor<DeleteRuleStatement> {

    public DeleteRuleStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeleteRuleStatement visitDeleteRuleStatement(PMLParser.DeleteRuleStatementContext ctx) {
        Expression<String> ruleNameExpr = ExpressionVisitor.compile(visitorCtx, ctx.ruleName, STRING_TYPE);
        Expression<String> oblNameExpr = ExpressionVisitor.compile(visitorCtx, ctx.obligationName, STRING_TYPE);
        return new DeleteRuleStatement(ruleNameExpr, oblNameExpr);
    }
}
