package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class ContinueStatement extends PALStatement {
    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return Value.continueValue();
    }

    @Override
    public String toString() {
        return "continue;";
    }
}
