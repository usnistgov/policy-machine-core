package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.AssignStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

public class AssignStmtVisitor extends PMLBaseVisitor<AssignStatement> {

    public AssignStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public AssignStatement visitAssignStatement(PMLParser.AssignStatementContext ctx) {
        Expression ascendant = Expression.compile(visitorCtx, ctx.ascendantNode, Type.string());
        Expression descendants = Expression.compile(visitorCtx, ctx.descendantNodes, Type.array(Type.string()));

        return new AssignStatement(ascendant, descendants);
    }
}
