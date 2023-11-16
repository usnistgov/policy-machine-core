package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.DeassignStatement;
import gov.nist.csd.pm.policy.pml.type.Type;


public class DeassignStmtVisitor extends PMLBaseVisitor<DeassignStatement> {

    public DeassignStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeassignStatement visitDeassignStatement(PMLParser.DeassignStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.childNode, Type.string());
        Expression parents = Expression.compile(visitorCtx, ctx.parentNodes, Type.array(Type.string()));

        return new DeassignStatement(name, parents);
    }
}