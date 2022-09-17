package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;
import gov.nist.csd.pm.policy.author.pal.statement.DeleteRuleStatement;

public class DeleteRuleStmtVisitor extends PALBaseVisitor<DeleteRuleStatement> {

    private final VisitorContext visitorCtx;

    public DeleteRuleStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DeleteRuleStatement visitDeleteRuleStmt(PALParser.DeleteRuleStmtContext ctx) {
        NameExpression ruleNameExpr = NameExpression.compile(visitorCtx, ctx.ruleName);
        NameExpression oblNameExpr = NameExpression.compile(visitorCtx, ctx.obligationName);
        return new DeleteRuleStatement(ruleNameExpr, oblNameExpr);
    }
}
