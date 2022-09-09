package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.io.Serializable;

public abstract class PALStatement implements Serializable {

    private static final String SPACES = "    ";

    public PALStatement() {}

    public abstract Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException;

    public abstract String toString(int indent);

    protected String format(int indent, String s, String ... args) {
        String format = String.format(s, (Object[]) args);
        return String.format("%s%s", SPACES.repeat(indent), format);
    }
}
