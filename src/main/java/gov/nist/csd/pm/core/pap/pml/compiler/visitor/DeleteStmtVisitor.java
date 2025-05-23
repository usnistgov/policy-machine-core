package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteNodeStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteObligationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteProhibitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteStatement;



public class DeleteStmtVisitor extends PMLBaseVisitor<DeleteStatement<?>> {

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeleteStatement<?> visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        Expression nameExpr = ExpressionVisitor.compile(visitorCtx, ctx.expression(), STRING_TYPE);

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
