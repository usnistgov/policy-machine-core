package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.DeleteStatement;
import gov.nist.csd.pm.policy.pml.type.Type;


public class DeleteStmtVisitor extends PMLBaseVisitor<DeleteStatement> {

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeleteStatement visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        Expression nameExpr = Expression.compile(visitorCtx, ctx.expression(), Type.string());

        PMLParser.DeleteTypeContext deleteTypeCtx = ctx.deleteType();
        DeleteStatement.Type deleteType = null;
        if (deleteTypeCtx instanceof PMLParser.DeleteNodeContext deleteNodeCtx) {
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
        } else if (deleteTypeCtx instanceof PMLParser.DeleteObligationContext) {
            deleteType = DeleteStatement.Type.OBLIGATION;
        } else if (deleteTypeCtx instanceof PMLParser.DeleteFunctionContext) {
            deleteType = DeleteStatement.Type.FUNCTION;
        } else {
            deleteType = DeleteStatement.Type.CONST;
        }

        return new DeleteStatement(deleteType, nameExpr);
    }
}
