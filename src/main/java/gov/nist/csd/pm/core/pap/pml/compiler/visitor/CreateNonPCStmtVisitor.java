package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateNonPCStatement;
import java.util.List;


import static gov.nist.csd.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;


public class CreateNonPCStmtVisitor extends PMLBaseVisitor<CreateNonPCStatement> {

    public CreateNonPCStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateNonPCStatement visitCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx) {
        NodeType type = getNodeType(ctx.nonPCNodeType());
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.name, STRING_TYPE);
        Expression<List<String>> assignTo = ExpressionVisitor.compile(visitorCtx, ctx.in, ListType.of(STRING_TYPE));

        return new CreateNonPCStatement(name, type, assignTo);
    }

    private NodeType getNodeType(PMLParser.NonPCNodeTypeContext nodeType) {
        if (nodeType.OA() != null) {
            return OA;
        } else if (nodeType.UA() != null) {
            return NodeType.UA;
        } else if (nodeType.O() != null) {
            return NodeType.O;
        } else {
            return NodeType.U;
        }
    }
}
