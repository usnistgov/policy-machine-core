package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteNodeStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteObligationStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteProhibitionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteStatement;
import gov.nist.csd.pm.pap.pml.type.Type;


public class DeleteStmtVisitor extends PMLBaseVisitor<DeleteStatement<?>> {

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeleteStatement<?> visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        Expression nameExpr = Expression.compile(visitorCtx, ctx.expression(), Type.string());

        PMLParser.DeleteTypeContext deleteTypeCtx = ctx.deleteType();
        if (deleteTypeCtx instanceof PMLParser.DeleteNodeContext) {
           return new DeleteNodeStatement(nameExpr);
        } else if (deleteTypeCtx instanceof PMLParser.DeleteProhibitionContext) {
            return new DeleteProhibitionStatement(nameExpr);
        } else {
            return new DeleteObligationStatement(nameExpr);
        }
    }
}
