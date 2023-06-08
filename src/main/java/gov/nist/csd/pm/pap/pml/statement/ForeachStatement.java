package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PolicyPoint;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ForeachStatement extends ControlStatement {

    private final String varName;
    private final String valueVarName;
    private final Expression iter;
    private final List<PMLStatement> statements;

    public ForeachStatement(String varName, String valueVarName, Expression iter, List<PMLStatement> statements) {
        this.varName = varName;
        this.valueVarName = valueVarName;
        this.iter = iter;
        this.statements = statements;
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        if (statements.isEmpty()) {
            return new VoidValue();
        }

        Value iterValue = iter.execute(ctx, pap);
        if (iterValue instanceof ArrayValue arrayValue) {
            return executeArrayIterator(ctx, arrayValue);
        } else if (iterValue instanceof MapValue mapValue) {
            return executeMapIterator(ctx, mapValue, pap);
        }

        return new VoidValue();
    }

    private Value executeArrayIterator(ExecutionContext ctx, ArrayValue iterValue) throws PMException{
        for (Value v : iterValue.getValue()) {
            Value value = ctx.executeStatements(statements, Map.of(varName, v));

            if (value instanceof BreakValue) {
                break;
            } else if (value instanceof ReturnValue) {
                return value;
            }
        }
        return new VoidValue();
    }

    private Value executeMapIterator(ExecutionContext ctx, MapValue iterValue, PAP pap) throws PMException{
        for (Value key : iterValue.getValue().keySet()) {
            Value mapValue = iterValue.getMapValue().get(key);

            Map<String, Object> operands = new HashMap<>(Map.of(varName, key));
            if (valueVarName != null) {
                operands.put(valueVarName, mapValue);
            }

            Value value = ctx.executeStatements(statements, operands);

            if (value instanceof BreakValue) {
                break;
            } else if (value instanceof ReturnValue) {
                return value;
            }
        }

        return new VoidValue();
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
