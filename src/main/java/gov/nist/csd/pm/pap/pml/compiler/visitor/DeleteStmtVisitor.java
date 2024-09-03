package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteStatement;
import gov.nist.csd.pm.pap.pml.type.Type;


public class DeleteStmtVisitor extends PMLBaseVisitor<DeleteStatement> {

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeleteStatement visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        Expression nameExpr = Expression.compile(visitorCtx, ctx.expression(), Type.string());

        PMLParser.DeleteTypeContext deleteTypeCtx = ctx.deleteType();
        DeleteStatement.Type deleteType = null;
        if (deleteTypeCtx instanceof PMLParser.DeleteNodeContext) {
            PMLParser.DeleteNodeContext deleteNodeCtx = (PMLParser.DeleteNodeContext) deleteTypeCtx;
            PMLParser.NodeTypeContext nodeTypeCtx = deleteNodeCtx.nodeType();
            if (nodeTypeCtx.POLICY_CLASS() != null) {
                deleteType = DeleteStatement.Type.POLICY_CLASS;
            } else if (nodeTypeCtx.OBJECT_ATTRIBUTE() != null) {
                deleteType = DeleteStatement.Type.OBJECT_ATTRIBUTE;
            } else if (nodeTypeCtx.USER_ATTRIBUTE() != null) {
                deleteType = DeleteStatement.Type.USER_ATTRIBUTE;
            } else if (nodeTypeCtx.OBJECT() != null) {
                deleteType = DeleteStatement.Type.OBJECT;
            } else {
                deleteType = DeleteStatement.Type.USER;
            }
        } else if (deleteTypeCtx instanceof PMLParser.DeleteProhibitionContext) {
            deleteType = DeleteStatement.Type.PROHIBITION;
        } else {
            deleteType = DeleteStatement.Type.OBLIGATION;
        }

        return new DeleteStatement(deleteType, nameExpr);
    }
}
