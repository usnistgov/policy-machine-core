package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.DeassignStatement;
import java.util.List;


/**
 * Compiles a PML "deassign ... from ..." statement into a {@link DeassignStatement}.
 */
public class DeassignStmtVisitor extends PMLBaseVisitor<DeassignStatement> {

    public DeassignStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeassignStatement visitDeassignStatement(PMLParser.DeassignStatementContext ctx) {
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.ascendantNode, STRING_TYPE);
        Expression<List<String>> descendants = ExpressionVisitor.compile(visitorCtx, ctx.descendantNodes, ListType.of(STRING_TYPE));

        return new DeassignStatement(name, descendants);
    }
}