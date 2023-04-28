package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.AssignStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;

public class AssignStmtVisitor extends PMLBaseVisitor<AssignStatement> {

    private final VisitorContext visitorCtx;

    public AssignStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public AssignStatement visitAssignStatement(PMLParser.AssignStatementContext ctx) {
        Expression child = Expression.compile(visitorCtx, ctx.childNode, Type.string());
        Expression parents = Expression.compile(visitorCtx, ctx.parentNodes, Type.array(Type.string()));

        return new AssignStatement(child, parents);
    }
}
