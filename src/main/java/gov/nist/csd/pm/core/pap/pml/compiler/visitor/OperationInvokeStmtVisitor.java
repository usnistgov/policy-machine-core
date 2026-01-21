package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;

public class OperationInvokeStmtVisitor extends PMLBaseVisitor<Expression<?>> {

    public OperationInvokeStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public Expression<?> visitOperationInvokeStatement(PMLParser.OperationInvokeStatementContext ctx) {
        return ExpressionVisitor.compileOperationInvoke(visitorCtx, ctx.operationInvoke(), ANY_TYPE);
    }
}