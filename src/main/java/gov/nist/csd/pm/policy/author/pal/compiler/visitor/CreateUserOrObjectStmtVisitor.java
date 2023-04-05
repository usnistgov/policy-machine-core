package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.CreateUserOrObjectStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

public class CreateUserOrObjectStmtVisitor extends PALBaseVisitor<CreateUserOrObjectStatement> {

    private final VisitorContext visitorCtx;

    public CreateUserOrObjectStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateUserOrObjectStatement visitCreateUserOrObjectStmt(PALParser.CreateUserOrObjectStmtContext ctx) {
        NodeType type = ctx.OBJECT() != null ?
                NodeType.O : NodeType.U;
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());
        Expression assignTo = Expression.compile(visitorCtx, ctx.parents, Type.array(Type.string()));

        return new CreateUserOrObjectStatement(name, type, assignTo);
    }
}
