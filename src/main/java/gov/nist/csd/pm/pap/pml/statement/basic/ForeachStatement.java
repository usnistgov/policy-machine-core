package gov.nist.csd.pm.pap.pml.statement.basic;

import com.sun.jdi.VoidValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.function.arg.type.ListType;
import gov.nist.csd.pm.pap.function.arg.type.MapType;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.result.BreakResult;
import gov.nist.csd.pm.pap.pml.statement.result.ReturnResult;
import gov.nist.csd.pm.pap.pml.statement.result.StatementResult;

import gov.nist.csd.pm.pap.pml.statement.result.VoidResult;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ForeachStatement extends BasicStatement<StatementResult> {

    private final String varName;
    private final String valueVarName;
    private final Expression<?> iter;
    private final List<PMLStatement<?>> statements;

    public ForeachStatement(String varName, String valueVarName, Expression<?> iter, List<PMLStatement<?>> statements) {
        this.varName = varName;
        this.valueVarName = valueVarName;
        this.iter = iter;
        this.statements = statements;
    }

    @Override
    public StatementResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        if (statements.isEmpty()) {
            return new VoidResult();
        }

        ArgType<?> iterType = iter.getType();
        Object iterValue = iter.execute(ctx, pap);
        if (iterValue instanceof List<?> list && iterType instanceof ListType<?> listType) {
            return executeArrayIterator(ctx, list, listType);
        } else if (iterValue instanceof Map<?, ?> map && iterType instanceof MapType<?, ?> mapType) {
            return executeMapIterator(ctx, map, mapType);
        }

        return new VoidResult();
    }

    private StatementResult executeArrayIterator(ExecutionContext ctx, List<?> iterValue, ListType<?> listType) throws PMException{
        for (Object o : iterValue) {
            Args args = new Args();
            args.put(new FormalParameter<>(varName, listType.getElementType()), o);

            StatementResult value = ctx.executeStatements(statements, args);

            if (value instanceof BreakResult) {
                break;
            } else if (value instanceof ReturnResult) {
                return value;
            }
        }
        return new VoidResult();
    }

    private StatementResult executeMapIterator(ExecutionContext ctx, Map<?, ?> iterValue, MapType<?, ?> mapType) throws PMException{
        for (Object key : iterValue.keySet()) {
            Object value = iterValue.get(key);

            // add the key value
            Args args = new Args();
            args.put(new FormalParameter<>(varName, ArgType.resolveTypeOfObject(key)), key);

            // add the value value
            if (valueVarName != null) {
                args.put(new FormalParameter<>(valueVarName, mapType.getValueType()), value);
            }

            StatementResult result = ctx.executeStatements(statements, args);

            if (result instanceof BreakResult) {
                break;
            } else if (result instanceof ReturnResult) {
                return result;
            }
        }

        return new VoidResult();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%sforeach %s in %s %s",
                indent(indentLevel), (valueVarName != null ? String.format("%s, %s", varName, valueVarName) : varName),
                iter,
                new PMLStatementBlock(statements).toFormattedString(indentLevel)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForeachStatement that)) return false;
        return Objects.equals(varName, that.varName) && Objects.equals(valueVarName, that.valueVarName) && Objects.equals(iter, that.iter) && Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName, valueVarName, iter, statements);
    }
} 