package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.CreatePolicyStatement;

public class CreatePolicyStmtVisitor extends PALBaseVisitor<CreatePolicyStatement> {

    private final VisitorContext visitorCtx;

    public CreatePolicyStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreatePolicyStatement visitCreatePolicyStmt(PALParser.CreatePolicyStmtContext ctx) {
        Expression expr = Expression.compile(visitorCtx, ctx.name, Type.string());
        return new CreatePolicyStatement(expr);
    }
}
