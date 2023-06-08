package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateNonPCStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

import static gov.nist.csd.pm.common.graph.node.NodeType.OA;

public class CreateNonPCStmtVisitor extends PMLBaseVisitor<CreateNonPCStatement> {

    public CreateNonPCStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateNonPCStatement visitCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx) {
        NodeType type = getNodeType(ctx.nonPCNodeType());
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());
        Expression assignTo = Expression.compile(visitorCtx, ctx.in, Type.array(Type.string()));

        return new CreateNonPCStatement(name, type, assignTo);
    }

    private NodeType getNodeType(PMLParser.NonPCNodeTypeContext nodeType) {
        if (nodeType.OBJECT_ATTRIBUTE() != null) {
            return OA;
        } else if (nodeType.USER_ATTRIBUTE() != null) {
            return NodeType.UA;
        } else if (nodeType.OBJECT() != null) {
            return NodeType.O;
        } else {
            return NodeType.U;
        }
    }
}
