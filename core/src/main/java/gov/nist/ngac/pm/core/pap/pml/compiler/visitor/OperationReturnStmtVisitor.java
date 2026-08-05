package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.ReturnStatement;

public class OperationReturnStmtVisitor extends PMLBaseVisitor<ReturnStatement> {

    public OperationReturnStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public ReturnStatement visitReturnStatement(PMLParser.ReturnStatementContext ctx) {
        if (ctx.expression() == null) {
            return new ReturnStatement();
        }

        Expression<?> e = ExpressionVisitor.compile(visitorCtx, ctx.expression(), ANY_TYPE);

        return new ReturnStatement(e);
    }
}
