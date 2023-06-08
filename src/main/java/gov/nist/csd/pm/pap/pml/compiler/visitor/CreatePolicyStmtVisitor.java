package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

public class CreatePolicyStmtVisitor extends PMLBaseVisitor<CreatePolicyStatement> {

    public CreatePolicyStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreatePolicyStatement visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());

        return new CreatePolicyStatement(name);
    }
}
