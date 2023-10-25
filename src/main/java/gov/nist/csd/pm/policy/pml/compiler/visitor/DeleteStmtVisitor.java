package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.DeleteStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;


public class DeleteStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        Expression nameExpr = Expression.compile(visitorCtx, ctx.expression(), Type.string());

        PMLParser.DeleteTypeContext deleteTypeCtx = ctx.deleteType();
        if (deleteTypeCtx instanceof PMLParser.DeleteNodeContext deleteNodeCtx) {
            DeleteStatement.Type deleteNodeType;
            PMLParser.NodeTypeContext nodeTypeCtx = deleteNodeCtx.nodeType();
            if (nodeTypeCtx.POLICY_CLASS() != null) {
                deleteNodeType = DeleteStatement.Type.POLICY_CLASS;
            } else if (nodeTypeCtx.OBJECT_ATTRIBUTE() != null) {
                deleteNodeType = DeleteStatement.Type.OBJECT_ATTRIBUTE;
            } else if (nodeTypeCtx.USER_ATTRIBUTE() != null) {
                deleteNodeType = DeleteStatement.Type.USER_ATTRIBUTE;
            } else if (nodeTypeCtx.OBJECT() != null) {
                deleteNodeType = DeleteStatement.Type.OBJECT;
            } else {
                deleteNodeType = DeleteStatement.Type.USER;
            }

            return new DeleteStatement(deleteNodeType, nameExpr);
        } else if (deleteTypeCtx instanceof PMLParser.DeleteProhibitionContext) {
            return new DeleteStatement(DeleteStatement.Type.PROHIBITION, nameExpr);
        } else {
            return new DeleteStatement(DeleteStatement.Type.OBLIGATION, nameExpr);
        }
    }
}
