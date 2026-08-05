package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.compiler.error.CompileError;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.scope.PMLScopeException;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.ForeachStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Compiles a PML "foreach" statement into a {@link ForeachStatement}, binding the loop variable(s) to
 * the element type of the iterated list or the key/value types of the iterated map.
 */
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
        List<CompileError> bodyErrors = new ArrayList<>();
        for (PMLParser.StatementContext stmtCtx : ctx.statementBlock().statement()) {
            try {
                block.add(new StatementVisitor(localVisitorCtx).visitStatement(stmtCtx));
            } catch (PMLCompilationRuntimeException e) {
                bodyErrors.addAll(e.getErrors());
            }
            visitorCtx.scope().overwriteFromScope(localVisitorCtx.scope());
        }

        if (!bodyErrors.isEmpty()) {
            throw new PMLCompilationRuntimeException(bodyErrors);
        }

        return new ForeachStatement(varName, mapValueVarName, iter, block);
    }
}
