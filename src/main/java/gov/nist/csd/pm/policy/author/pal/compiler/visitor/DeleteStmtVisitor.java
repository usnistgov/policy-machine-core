package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.DeleteStatement;

public class DeleteStmtVisitor extends PALBaseVisitor<DeleteStatement> {

    private final VisitorContext visitorCtx;

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DeleteStatement visitDeleteStmt(PALParser.DeleteStmtContext ctx) {
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

            return new DeleteStatement(deleteNodeType, Expression.compile(visitorCtx, ctx.expression(), Type.string()));
        } else if (deleteTypeCtx instanceof PALParser.DeleteProhibitionContext) {
            return new DeleteStatement(DeleteStatement.Type.PROHIBITION, Expression.compile(visitorCtx, ctx.expression(), Type.string()));
        } else if (deleteTypeCtx instanceof PALParser.DeleteObligationContext) {
            return new DeleteStatement(DeleteStatement.Type.OBLIGATION, Expression.compile(visitorCtx, ctx.expression(), Type.string()));
        } else {
            visitorCtx.errorLog().addError(
                    ctx,
                    "invalid delete statement"
            );
            return new DeleteStatement(DeleteStatement.Type.POLICY_CLASS, new Expression(new Literal("")));
        }
    }
}
