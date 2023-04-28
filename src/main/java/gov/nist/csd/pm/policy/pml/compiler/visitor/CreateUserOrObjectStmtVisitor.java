package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.CreateUserOrObjectStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

public class CreateUserOrObjectStmtVisitor extends PMLBaseVisitor<CreateUserOrObjectStatement> {

    private final VisitorContext visitorCtx;

    public CreateUserOrObjectStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateUserOrObjectStatement visitCreateUserOrObjectStatement(PMLParser.CreateUserOrObjectStatementContext ctx) {
        NodeType type = ctx.OBJECT() != null ?
                NodeType.O : NodeType.U;
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());
        Expression assignTo = Expression.compile(visitorCtx, ctx.parents, Type.array(Type.string()));

        return new CreateUserOrObjectStatement(name, type, assignTo);
    }
}
