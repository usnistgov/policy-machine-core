package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;

public class CreatePolicyStmtVisitor extends PMLBaseVisitor<CreatePolicyStatement> {

    private final VisitorContext visitorCtx;

    public CreatePolicyStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreatePolicyStatement visitCreatePolicyStmt(PMLParser.CreatePolicyStmtContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.expression(), Type.string());
        return new CreatePolicyStatement(name);
    }
}
