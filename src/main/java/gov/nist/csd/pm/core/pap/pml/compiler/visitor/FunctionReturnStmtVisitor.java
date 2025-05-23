package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ReturnStatement;
import org.antlr.v4.runtime.ParserRuleContext;

public class FunctionReturnStmtVisitor extends PMLBaseVisitor<ReturnStatement> {

    public FunctionReturnStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public ReturnStatement visitReturnStatement(PMLParser.ReturnStatementContext ctx) {
        ParserRuleContext enclosingCtx = getEnclosingContext(ctx);
        if (enclosingCtx == null) {
            throw new PMLCompilationRuntimeException(
                    ctx,
                    "return statement not in function definition or obligation response"
            );
        }

        if (ctx.expression() == null) {
            return new ReturnStatement();
        } else if (enclosingCtx instanceof PMLParser.ResponseContext) {
            throw new PMLCompilationRuntimeException(
                    ctx,
                    "return statement in response cannot return a value"
            );
        }

        Expression<?> e = ExpressionVisitor.compile(visitorCtx, ctx.expression(), ANY_TYPE);

        return new ReturnStatement(e);
    }

    private ParserRuleContext getEnclosingContext(ParserRuleContext ctx) {
        if (ctx instanceof PMLParser.BasicFunctionDefinitionStatementContext ||
                ctx instanceof PMLParser.OperationDefinitionStatementContext ||
                ctx instanceof PMLParser.RoutineDefinitionStatementContext ||
                ctx instanceof PMLParser.ResponseContext) {
            return ctx;
        } else if (ctx == null) {
            return null;
        }

        return getEnclosingContext(ctx.getParent());
    }
}
