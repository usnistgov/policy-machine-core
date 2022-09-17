package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.scope.PALScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.ForeachStatement;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;

import java.util.ArrayList;
import java.util.List;

public class ForeachStmtVisitor extends PALBaseVisitor<ForeachStatement> {

    private final VisitorContext visitorCtx;

    public ForeachStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public ForeachStatement visitForeachStmt(PALParser.ForeachStmtContext ctx) {
        Type anyArrayType = Type.array(Type.any());
        Type anyMapType = Type.map(Type.any(), Type.any());

        Expression iter = Expression.compile(visitorCtx, ctx.expression(), anyArrayType, anyMapType);
        Type iterType = Type.any();
        try {
            iterType = iter.getType(visitorCtx.scope());
        } catch (PALScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        String varName = ctx.key.getText();
        String mapValueVarName = null;
        if (ctx.mapValue != null) {
            if (!iterType.equals(Type.map(Type.any(), Type.any()))) {
                visitorCtx.errorLog().addError(
                        ctx,
                        "use of key, value in foreach only available for maps"
                );
            } else {
                mapValueVarName = ctx.mapValue.getText();
            }
        }

        List<PALStatement> block = new ArrayList<>();
        Type keyType;
        Type valueType = null;
        if (iterType.equals(anyArrayType)) {
            keyType = iterType.getArrayType();
        } else {
            keyType = iterType.getMapKeyType();
            valueType = iterType.getMapValueType();
        }

        for (PALParser.StmtContext stmtCtx : ctx.stmtBlock().stmt()) {
            VisitorContext localVisitorCtx = visitorCtx.copy();

            try {
                localVisitorCtx.scope().addVariable(varName, keyType, false);
                if (valueType != null) {
                    localVisitorCtx.scope().addVariable(mapValueVarName, valueType, false);
                }
            }catch (PALScopeException e) {
                visitorCtx.errorLog().addError(ctx, e.getMessage());
            }

            PALStatement statement = new StatementVisitor(localVisitorCtx)
                    .visitStmt(stmtCtx);
            block.add(statement);

            visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());
        }

        return new ForeachStatement(varName, mapValueVarName, iter, block);
    }
}
