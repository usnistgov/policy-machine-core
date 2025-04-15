package gov.nist.csd.pm.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.listType;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.AssignStatement;

import java.util.List;

public class AssignStmtVisitor extends PMLBaseVisitor<AssignStatement> {

    public AssignStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public AssignStatement visitAssignStatement(PMLParser.AssignStatementContext ctx) {
        Expression<String> ascendant = ExpressionVisitor.compile(visitorCtx, ctx.ascendantNode, STRING_TYPE);
        Expression<List<String>> descendants = ExpressionVisitor.compile(visitorCtx, ctx.descendantNodes, listType(STRING_TYPE));

        return new AssignStatement(ascendant, descendants);
    }
}
