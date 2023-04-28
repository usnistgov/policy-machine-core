package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.io.Serializable;

public abstract class PMLStatement implements Serializable {

    public PMLStatement() {}

    public abstract Value execute(ExecutionContext ctx, Policy policy) throws PMException;

    @Override
    public abstract String toString();
}
