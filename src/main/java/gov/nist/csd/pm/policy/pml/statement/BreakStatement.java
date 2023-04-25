package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class BreakStatement extends PMLStatement {
    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return Value.breakValue();
    }

    @Override
    public String toString() {
        return "break";
    }
}
