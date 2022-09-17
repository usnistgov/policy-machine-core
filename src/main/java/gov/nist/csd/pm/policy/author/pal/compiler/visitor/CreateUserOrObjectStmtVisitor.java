package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;
import gov.nist.csd.pm.policy.author.pal.statement.CreateUserOrObjectStatement;
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
        NameExpression name = NameExpression.compile(visitorCtx, ctx.nameExpression());
        NameExpression assignTo = NameExpression.compileArray(visitorCtx, ctx.nameExpressionArray());

        return new CreateUserOrObjectStatement(name, type, assignTo);
    }
}
