package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;



import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ForeachStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;


import java.util.ArrayList;
import java.util.List;

public class ForeachStmtVisitor extends PMLBaseVisitor<ForeachStatement> {

    public ForeachStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public ForeachStatement visitForeachStatement(PMLParser.ForeachStatementContext ctx) {
        boolean isMapFor = ctx.value != null;

        Expression<?> iter;
        Type<?> keyType;
        Type<?> valueType = null;

        if (isMapFor) {
            iter = ExpressionVisitor.compile(visitorCtx, ctx.expression(), MapType.of(ANY_TYPE, ANY_TYPE));

            MapType<?, ?> actualMapType = (MapType<?, ?>) iter.getType();
            keyType = actualMapType.getKeyType();
            valueType = actualMapType.getValueType();
        } else {
            iter = ExpressionVisitor.compile(visitorCtx, ctx.expression(), ListType.of(ANY_TYPE));

            ListType<?> actualListType = (ListType<?>) iter.getType();
            keyType = actualListType.getElementType();
        }

        String varName = ctx.key.getText();
        String mapValueVarName = isMapFor ? ctx.value.getText() : null;

        VisitorContext localVisitorCtx = visitorCtx.copy();
        try {
            localVisitorCtx.scope().addVariable(varName, new Variable(varName, keyType, false));
            if (valueType != null) {
                localVisitorCtx.scope().addVariable(mapValueVarName, new Variable(mapValueVarName, valueType, false));
            }
        }catch (PMLScopeException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }

        List<PMLStatement<?>> block = new ArrayList<>();
        for (PMLParser.StatementContext stmtCtx : ctx.statementBlock().statement()) {
            PMLStatement<?> statement = new StatementVisitor(localVisitorCtx)
                    .visitStatement(stmtCtx);
            block.add(statement);

            visitorCtx.scope().overwriteFromScope(localVisitorCtx.scope());
        }

        return new ForeachStatement(varName, mapValueVarName, iter, block);
    }
}
