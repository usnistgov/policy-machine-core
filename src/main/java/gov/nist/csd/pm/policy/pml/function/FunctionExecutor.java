package gov.nist.csd.pm.policy.pml.function;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.Value;

/**
 * This class is not serializable because code defined in the exec method could use dependencies not available
 * on the target system.
 */
public interface FunctionExecutor {

    Value exec(ExecutionContext ctx, Policy policy) throws PMException;

}
