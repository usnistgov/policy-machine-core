package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

public class CreatePolicyStmtVisitor extends PMLBaseVisitor<CreatePolicyClassStatement> {

    public CreatePolicyStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreatePolicyClassStatement visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());

        return new CreatePolicyClassStatement(name);
    }
}
