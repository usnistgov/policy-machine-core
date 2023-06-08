package gov.nist.csd.pm.policy.pml.model.function;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.io.Serializable;

public interface FunctionExecutor extends Serializable {

    Value exec(ExecutionContext ctx, Policy policy) throws PMException, PMLScopeException;

}
