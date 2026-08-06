package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;

/**
 * Compiles a PML operation invocation statement into an {@link Expression}.
 */
public class OperationInvokeStmtVisitor extends PMLBaseVisitor<Expression<?>> {

    public OperationInvokeStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public Expression<?> visitOperationInvokeStatement(PMLParser.OperationInvokeStatementContext ctx) {
        return ExpressionVisitor.compileOperationInvoke(visitorCtx, ctx.operationInvoke(), ANY_TYPE);
    }
}