package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;

import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.pml.PMLExecutor.executeStatementBlock;
import static gov.nist.csd.pm.policy.pml.PMLFormatter.statementsToString;

public class ForeachStatement extends PMLStatement {

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
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        if (statements.isEmpty()) {
            return new Value();
        }

        Value iterValue = iter.execute(ctx, policy);
        if (iterValue.isArray()) {
            for (Value v : iterValue.getArrayValue()) {
                ExecutionContext localExecutionCtx;
                try {
                    localExecutionCtx = ctx.copy();
                } catch (PMLScopeException e) {
                    throw new RuntimeException(e);
                }

                localExecutionCtx.scope().putValue(varName, v);

                Value value = executeStatementBlock(localExecutionCtx, policy, statements);

                if (value.isBreak()) {
                    break;
                } else if (value.isReturn()) {
                    return value;
                }

                ctx.scope().overwriteValues(localExecutionCtx.scope());
            }
        } else if (iterValue.isMap()) {
            for (Value key : iterValue.getMapValue().keySet()) {
                ExecutionContext localExecutionCtx;
                try {
                    localExecutionCtx = ctx.copy();
                } catch (PMLScopeException e) {
                    throw new RuntimeException(e);
                }

                Value mapValue = iterValue.getMapValue().get(key);

                localExecutionCtx.scope().putValue(varName, key);
                if (valueVarName != null) {
                    localExecutionCtx.scope().putValue(valueVarName, mapValue);
                }

                Value value = executeStatementBlock(localExecutionCtx, policy, statements);

                if (value.isBreak()) {
                    break;
                } else if (value.isReturn()) {
                    return value;
                }

                ctx.scope().overwriteValues(localExecutionCtx.scope());
            }
        }

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("foreach %s in %s {%s}",
                (valueVarName != null ? String.format("%s, %s", varName, valueVarName) : varName),
                iter,
                statementsToString(statements)
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
