package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;

public class EmptyLineStatement extends PMLStatement{
    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new Value();
    }

    @Override
    public String toString() {
        return "\n";
    }
}
