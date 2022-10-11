package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.DeleteStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

public class DeleteStmtVisitor extends PALBaseVisitor<DeleteStatement> {

    private final VisitorContext visitorCtx;

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DeleteStatement visitDeleteStmt(PALParser.DeleteStmtContext ctx) {
        Expression nameExpr = Expression.compile(visitorCtx, ctx.expression(), Type.string());

        PALParser.DeleteTypeContext deleteTypeCtx = ctx.deleteType();
        if (deleteTypeCtx instanceof PALParser.DeleteNodeContext deleteNodeCtx) {
            DeleteStatement.Type deleteNodeType;
            PALParser.NodeTypeContext nodeTypeCtx = deleteNodeCtx.nodeType();
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
        } else if (deleteTypeCtx instanceof PALParser.DeleteProhibitionContext) {
            return new DeleteStatement(DeleteStatement.Type.PROHIBITION, nameExpr);
        } else {
            return new DeleteStatement(DeleteStatement.Type.OBLIGATION, nameExpr);
        }
    }
}
