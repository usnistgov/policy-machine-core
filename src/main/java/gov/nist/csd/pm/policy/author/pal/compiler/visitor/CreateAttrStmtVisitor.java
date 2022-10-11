package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.CreateAttrStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

public class CreateAttrStmtVisitor extends PALBaseVisitor<CreateAttrStatement> {

    private final VisitorContext visitorCtx;

    public CreateAttrStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateAttrStatement visitCreateAttrStmt(PALParser.CreateAttrStmtContext ctx) {
        NodeType type = ctx.OBJECT_ATTRIBUTE() != null ?
                NodeType.OA : NodeType.UA;
        Expression name = Expression.compile(visitorCtx, ctx.expression(), Type.string());
        Expression assignTo = Expression.compileArray(visitorCtx, ctx.expressionArray(), Type.string());

        return new CreateAttrStatement(name, type, assignTo);
    }
}
