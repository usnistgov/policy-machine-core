package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.io.Serializable;

public abstract class PMLStatement implements Serializable {

    public PMLStatement() {}

    public abstract Value execute(ExecutionContext ctx, Policy policy) throws PMException;

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public final String toString() {
        return toFormattedString(0);
    }

    public abstract String toFormattedString(int indentLevel);

    public static String indent(int indentLevel) {
        String INDENT = "    ";
        return INDENT.repeat(indentLevel);
    }

}
