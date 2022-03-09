package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.io.Serializable;

public abstract class PALStatement implements Serializable {

    public PALStatement() {}

    public abstract Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException;

}