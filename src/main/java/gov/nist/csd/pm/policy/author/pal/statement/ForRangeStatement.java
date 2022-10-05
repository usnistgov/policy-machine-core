package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.scope.PALScopeException;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

import static gov.nist.csd.pm.policy.author.pal.PALExecutor.executeStatementBlock;
import static gov.nist.csd.pm.policy.author.pal.PALFormatter.statementsToString;

public class ForRangeStatement extends PALStatement{

    private final String varName;
    private final int lower;
    private final int upper;
    private final List<PALStatement> block;

    public ForRangeStatement(String varName, int lower, int upper, List<PALStatement> block) {
        this.varName = varName;
        this.lower = lower;
        this.upper = upper;
        this.block = block;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        if (block.isEmpty()) {
            return new Value();
        }

        for (int i = lower; i <= upper; i++) {
            ExecutionContext localExecutionCtx;
            try {
                localExecutionCtx = ctx.copy();
            } catch (PALScopeException e) {
                throw new RuntimeException(e);
            }

            localExecutionCtx.scope().addValue(varName, new Value(i));

            Value value = executeStatementBlock(localExecutionCtx, policyAuthor, block);

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
        return String.format("for %s in range [%d, %d] {%s}",
                varName, lower, upper, statementsToString(block)
        );
    }
}
