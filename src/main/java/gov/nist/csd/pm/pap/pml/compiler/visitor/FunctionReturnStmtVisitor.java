package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.basic.FunctionReturnStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.antlr.v4.runtime.ParserRuleContext;

public class FunctionReturnStmtVisitor extends PMLBaseVisitor<FunctionReturnStatement> {

    public FunctionReturnStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public FunctionReturnStatement visitReturnStatement(PMLParser.ReturnStatementContext ctx) {
        ParserRuleContext enclosingCtx = getEnclosingContext(ctx);
        if (enclosingCtx == null) {
            throw new PMLCompilationRuntimeException(
                    ctx,
                    "return statement not in function definition or obligation response"
            );
        }

        if (ctx.expression() == null) {
            return new FunctionReturnStatement();
        } else if (enclosingCtx instanceof PMLParser.ResponseContext) {
            throw new PMLCompilationRuntimeException(
                    ctx,
                    "return statement in response cannot return a value"
            );
        }

        Expression e = Expression.compile(visitorCtx, ctx.expression(), Type.any());

        return new FunctionReturnStatement(e);
    }

    private ParserRuleContext getEnclosingContext(ParserRuleContext ctx) {
        if (ctx instanceof PMLParser.FunctionDefinitionStatementContext ||
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
