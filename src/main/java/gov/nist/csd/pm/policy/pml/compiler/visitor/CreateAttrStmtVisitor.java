package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.CreateAttrStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

public class CreateAttrStmtVisitor extends PMLBaseVisitor<CreateAttrStatement> {

    private final VisitorContext visitorCtx;

    public CreateAttrStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateAttrStatement visitCreateAttributeStatement(PMLParser.CreateAttributeStatementContext ctx) {
        NodeType type = ctx.OBJECT_ATTRIBUTE() != null ?
                NodeType.OA : NodeType.UA;
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());
        Expression assignTo = Expression.compile(visitorCtx, ctx.parents, Type.array(Type.string()));

        return new CreateAttrStatement(name, type, assignTo);
    }
}
