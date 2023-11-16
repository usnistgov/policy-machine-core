package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.statement.ForeachStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class ForeachStmtVisitor extends PMLBaseVisitor<ForeachStatement> {

    public ForeachStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public ForeachStatement visitForeachStatement(PMLParser.ForeachStatementContext ctx) {
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

            return new ForeachStatement(ctx);
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
            localVisitorCtx.scope().addVariable(varName, new Variable(varName, keyType, false));
            if (valueType != null) {
                localVisitorCtx.scope().addVariable(mapValueVarName, new Variable(mapValueVarName, valueType, false));
            }
        }catch (PMLScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());

            return new ForeachStatement(ctx);
        }

        for (PMLParser.StatementContext stmtCtx : ctx.statementBlock().statement()) {
            PMLStatement statement = new StatementVisitor(localVisitorCtx)
                    .visitStatement(stmtCtx);
            block.add(statement);

            visitorCtx.scope().local().overwriteFromLocalScope(localVisitorCtx.scope().local());
        }

        return new ForeachStatement(varName, mapValueVarName, iter, block);
    }
}
