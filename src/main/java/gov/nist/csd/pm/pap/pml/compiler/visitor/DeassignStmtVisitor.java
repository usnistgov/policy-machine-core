package gov.nist.csd.pm.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.listType;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.DeassignStatement;
import java.util.List;


public class DeassignStmtVisitor extends PMLBaseVisitor<DeassignStatement> {

    public DeassignStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeassignStatement visitDeassignStatement(PMLParser.DeassignStatementContext ctx) {
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.ascendantNode, STRING_TYPE);
        Expression<List<String>> descendants = ExpressionVisitor.compile(visitorCtx, ctx.descendantNodes, listType(STRING_TYPE));

        return new DeassignStatement(name, descendants);
    }
}