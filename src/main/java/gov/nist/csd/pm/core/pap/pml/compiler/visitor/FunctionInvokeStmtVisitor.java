package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;

import gov.nist.csd.pm.core.pap.pml.expression.Expression;

public class FunctionInvokeStmtVisitor extends PMLBaseVisitor<Expression<?>> {

    public FunctionInvokeStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public Expression<?> visitFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx) {
        return ExpressionVisitor.compileFunctionInvoke(visitorCtx, ctx.functionInvoke(), ANY_TYPE);
    }
}