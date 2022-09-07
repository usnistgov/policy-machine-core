package gov.nist.csd.pm.policy.author.pal.model.function;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

public interface FunctionExecutor {

    Value exec(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException;

}
