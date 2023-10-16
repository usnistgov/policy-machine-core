package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.value.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.policy.pml.PMLExecutor.executeStatementBlock;

public class ForeachStatement extends PMLStatement {

    private String varName;
    private String valueVarName;
    private Expression iter;
    private List<PMLStatement> statements;

    public ForeachStatement(String varName, String valueVarName, Expression iter, List<PMLStatement> statements) {
        this.varName = varName;
        this.valueVarName = valueVarName;
        this.iter = iter;
        this.statements = statements;
    }

    public ForeachStatement(PMLParser.ForeachStatementContext ctx) {
        super(ctx);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        if (statements.isEmpty()) {
            return new VoidValue();
        }

        Value iterValue = iter.execute(ctx, policy);
        if (iterValue instanceof ArrayValue) {
            ArrayValue arrayValue = (ArrayValue) iterValue;

            for (Value v : arrayValue.getValue()) {
                ExecutionContext localExecutionCtx = ctx.copy();

                localExecutionCtx.scope().addVariable(varName, v);

                Value value = executeStatementBlock(localExecutionCtx, policy, statements);

                if (value instanceof BreakValue) {
                    break;
                } else if (value instanceof ReturnValue) {
                    return value;
                }

                ctx.scope().local().overwriteFromLocalScope(localExecutionCtx.scope().local());
            }
        } else if (iterValue instanceof MapValue) {
            MapValue mapValue = (MapValue) iterValue;

            for (Map.Entry<Value, Value> entry : mapValue.getValue().entrySet()) {
                ExecutionContext localExecutionCtx;
                try {
                    localExecutionCtx = ctx.copy();
                } catch (PMLScopeException e) {
                    throw new RuntimeException(e);
                }

                localExecutionCtx.scope().addVariable(varName, entry.getKey());
                if (valueVarName != null) {
                    localExecutionCtx.scope().addVariable(valueVarName, entry.getValue());
                }

                Value value = executeStatementBlock(localExecutionCtx, policy, statements);

                if (value instanceof BreakValue) {
                    break;
                } else if (value instanceof ReturnValue) {
                    return value;
                }

                ctx.scope().local().overwriteFromLocalScope(localExecutionCtx.scope().local());
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
        if (o == null || getClass() != o.getClass()) return false;
        ForeachStatement that = (ForeachStatement) o;
        return Objects.equals(varName, that.varName) && Objects.equals(valueVarName, that.valueVarName) && Objects.equals(iter, that.iter) && Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName, valueVarName, iter, statements);
    }

}
