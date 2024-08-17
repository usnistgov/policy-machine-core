package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.DeassignStatement;
import gov.nist.csd.pm.pap.pml.type.Type;


public class DeassignStmtVisitor extends PMLBaseVisitor<DeassignStatement> {

    public DeassignStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeassignStatement visitDeassignStatement(PMLParser.DeassignStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.ascendantNode, Type.string());
        Expression descendants = Expression.compile(visitorCtx, ctx.descendantNodes, Type.array(Type.string()));

        return new DeassignStatement(name, descendants);
    }
}