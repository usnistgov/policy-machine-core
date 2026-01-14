package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteAdminOpStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteNodeStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteObligationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteProhibitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteResourceOpStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteStatement;



public class DeleteStmtVisitor extends PMLBaseVisitor<DeleteStatement> {

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeleteStatement visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        Expression<String> nameExpr = ExpressionVisitor.compile(visitorCtx, ctx.expression(), STRING_TYPE);
        boolean ifExists = ctx.IF_EXISTS() != null;

        PMLParser.DeleteTypeContext deleteTypeCtx = ctx.deleteType();
        return switch (deleteTypeCtx) {
            case PMLParser.DeleteNodeContext deleteNodeContext ->
                new DeleteNodeStatement(nameExpr, ifExists);
            case PMLParser.DeleteProhibitionContext deleteProhibitionContext ->
                new DeleteProhibitionStatement(nameExpr, ifExists);
            case PMLParser.DeleteAdminOpContext deleteAdminOpContext ->
                new DeleteAdminOpStatement(nameExpr, ifExists);
            case PMLParser.DeleteResourceOpContext deleteResourceOpContext ->
                new DeleteResourceOpStatement(nameExpr, ifExists);
            case null, default ->
                new DeleteObligationStatement(nameExpr, ifExists);
        };
    }
}
