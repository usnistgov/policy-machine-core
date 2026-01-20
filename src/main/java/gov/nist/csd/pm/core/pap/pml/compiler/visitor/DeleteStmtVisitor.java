package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.DeleteNodeContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.DeleteObligationContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.DeleteOperationContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.DeleteProhibitionContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.DeleteStatementContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.DeleteTypeContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteNodeStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteObligationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteOperationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteProhibitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteStatement;



public class DeleteStmtVisitor extends PMLBaseVisitor<DeleteStatement> {

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeleteStatement visitDeleteStatement(DeleteStatementContext ctx) {
        Expression<String> nameExpr = ExpressionVisitor.compile(visitorCtx, ctx.expression(), STRING_TYPE);
        boolean ifExists = ctx.IF_EXISTS() != null;

        DeleteTypeContext deleteTypeCtx = ctx.deleteType();
        return switch (deleteTypeCtx) {
            case DeleteNodeContext deleteNodeContext -> new DeleteNodeStatement(nameExpr, ifExists);
            case DeleteProhibitionContext deleteProhibitionContext -> new DeleteProhibitionStatement(nameExpr, ifExists);
            case DeleteObligationContext deleteObligationContext -> new DeleteObligationStatement(nameExpr, ifExists);
            case DeleteOperationContext deleteAdminOpContext -> new DeleteOperationStatement(nameExpr, ifExists);
            default -> throw new IllegalStateException("Unexpected value: " + deleteTypeCtx);
        };
    }
}
