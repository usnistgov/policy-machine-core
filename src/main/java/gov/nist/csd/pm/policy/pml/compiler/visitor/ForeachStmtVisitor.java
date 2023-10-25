package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.statement.ErrorStatement;
import gov.nist.csd.pm.policy.pml.statement.ForeachStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class ForeachStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public ForeachStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitForeachStatement(PMLParser.ForeachStatementContext ctx) {
        boolean isMapFor = ctx.value != null;

        Expression iter;
        if (isMapFor) {
            iter = Expression.compile(visitorCtx, ctx.expression(), Type.map(Type.any(), Type.any()));
        } else {
            iter = Expression.compile(visitorCtx, ctx.expression(), Type.array(Type.any()));
        }

        Type iterType;
        try {
            iterType = iter.getType(visitorCtx.scope());
        } catch (PMLScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());

            return new ErrorStatement(ctx);
        }

        String varName = ctx.key.getText();
        String mapValueVarName = null;
        if (isMapFor) {
            mapValueVarName = ctx.value.getText();
        }

        List<PMLStatement> block = new ArrayList<>();
        Type keyType;
        Type valueType = null;
        if (isMapFor) {
            keyType = iterType.getMapKeyType();
            valueType = iterType.getMapValueType();
        } else {
            keyType = iterType.getArrayElementType();
        }

        VisitorContext localVisitorCtx = visitorCtx.copy();

        try {
            localVisitorCtx.scope().addVariable(varName, keyType, false);
            if (valueType != null) {
                localVisitorCtx.scope().addVariable(mapValueVarName, valueType, false);
            }
        }catch (PMLScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());

            return new ErrorStatement(ctx);
        }

        for (PMLParser.StatementContext stmtCtx : ctx.statementBlock().statement()) {
            PMLStatement statement = new StatementVisitor(localVisitorCtx)
                    .visitStatement(stmtCtx);
            block.add(statement);

            visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());
        }

        return new ForeachStatement(varName, mapValueVarName, iter, block);
    }
}
