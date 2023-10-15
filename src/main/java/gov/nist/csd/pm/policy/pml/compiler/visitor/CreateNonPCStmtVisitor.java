package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.CreateNonPCStatement;
public class CreateNonPCStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private VisitorContext visitorCtx;

    public CreateNonPCStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx) {
        NodeType type = getNodeType(ctx.nonPCNodeType());
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());
        Expression assignTo = Expression.compile(visitorCtx, ctx.assignTo, Type.array(Type.string()));

        if (ctx.properties == null) {
            return new CreateNonPCStatement(name, type, assignTo);
        }

        Expression withProperties = Expression.compile(visitorCtx, ctx.properties, Type.map(Type.string(), Type.string()));

        return new CreateNonPCStatement(name, type, assignTo, withProperties);
    }

    private NodeType getNodeType(PMLParser.NonPCNodeTypeContext nodeType) {
        if (nodeType.OBJECT_ATTRIBUTE() != null) {
            return NodeType.OA;
        } else if (nodeType.USER_ATTRIBUTE() != null) {
            return NodeType.UA;
        } else if (nodeType.OBJECT() != null) {
            return NodeType.O;
        } else {
            return NodeType.U;
        }
    }
}
