package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.AssignStatement;
import java.util.List;

public class AssignStmtVisitor extends PMLBaseVisitor<AssignStatement> {

    public AssignStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public AssignStatement visitAssignStatement(PMLParser.AssignStatementContext ctx) {
        Expression<String> ascendant = ExpressionVisitor.compile(visitorCtx, ctx.ascendantNode, STRING_TYPE);
        Expression<List<String>> descendants = ExpressionVisitor.compile(visitorCtx, ctx.descendantNodes, ListType.of(STRING_TYPE));

        return new AssignStatement(ascendant, descendants);
    }
}
