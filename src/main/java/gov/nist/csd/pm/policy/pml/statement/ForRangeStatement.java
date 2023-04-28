package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.PMLFormatter;

import java.util.List;

public class ForRangeStatement extends PMLStatement{

    private final String varName;
    private final Expression lower;
    private boolean lowerBound;
    private final Expression upper;
    private boolean upperBound;
    private final List<PMLStatement> block;

    public ForRangeStatement(String varName, Expression lower, boolean lowerBound, Expression upper, boolean upperBound, List<PMLStatement> block) {
        this.varName = varName;
        this.lower = lower;
        this.lowerBound = lowerBound;
        this.upper = upper;
        this.upperBound = upperBound;
        this.block = block;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        if (block.isEmpty()) {
            return new Value();
        }

        int lowerValue = lower.execute(ctx, policy).getNumberValue();
        if (lowerBound) {
            lowerValue++;
        }

        int upperValue = upper.execute(ctx, policy).getNumberValue();
        if (upperBound) {
            upperValue--;
        }

        for (int i = lowerValue; i <= upperValue; i++) {
            ExecutionContext localExecutionCtx;
            try {
                localExecutionCtx = ctx.copy();
            } catch (PMLScopeException e) {
                throw new RuntimeException(e);
            }

            localExecutionCtx.scope().putValue(varName, new Value(i));

            Value value = PMLExecutor.executeStatementBlock(localExecutionCtx, policy, block);

            if (value.isBreak()) {
                break;
            } else if (value.isReturn()) {
                return value;
            }

            ctx.scope().overwriteValues(localExecutionCtx.scope());
        }

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("for %s in range [%s, %s] {%s}",
                varName, lower, upper, PMLFormatter.statementsToString(block)
        );
    }
}
