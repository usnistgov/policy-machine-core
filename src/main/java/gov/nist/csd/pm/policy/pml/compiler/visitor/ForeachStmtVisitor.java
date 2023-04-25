package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.ForeachStatement;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

import java.util.ArrayList;
import java.util.List;

public class ForeachStmtVisitor extends PMLBaseVisitor<ForeachStatement> {

    private final VisitorContext visitorCtx;

    public ForeachStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public ForeachStatement visitForeachStmt(PMLParser.ForeachStmtContext ctx) {
        Type anyArrayType = Type.array(Type.any());
        Type anyMapType = Type.map(Type.any(), Type.any());

        Expression iter = Expression.compile(visitorCtx, ctx.expression(), anyArrayType, anyMapType);
        Type iterType = Type.any();
        try {
            iterType = iter.getType(visitorCtx.scope());
        } catch (PMLScopeException e) {
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

        List<PMLStatement> block = new ArrayList<>();
        Type keyType;
        Type valueType = null;
        if (iterType.equals(anyArrayType)) {
            keyType = iterType.getArrayType();
        } else {
            keyType = iterType.getMapKeyType();
            valueType = iterType.getMapValueType();
        }

        VisitorContext localVisitorCtx = visitorCtx.copy();
        for (PMLParser.StmtContext stmtCtx : ctx.stmtBlock().stmt()) {
            try {
                localVisitorCtx.scope().addVariable(varName, keyType, false);
                if (valueType != null) {
                    localVisitorCtx.scope().addVariable(mapValueVarName, valueType, false);
                }
            }catch (PMLScopeException e) {
                visitorCtx.errorLog().addError(ctx, e.getMessage());
            }

            PMLStatement statement = new StatementVisitor(localVisitorCtx)
                    .visitStmt(stmtCtx);
            block.add(statement);

            visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());
        }

        return new ForeachStatement(varName, mapValueVarName, iter, block);
    }
}
