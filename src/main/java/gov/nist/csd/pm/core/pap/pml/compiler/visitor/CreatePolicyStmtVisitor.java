package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;


public class CreatePolicyStmtVisitor extends PMLBaseVisitor<CreatePolicyClassStatement> {

    public CreatePolicyStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreatePolicyClassStatement visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx) {
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.name, STRING_TYPE);

        return new CreatePolicyClassStatement(name);
    }
}
