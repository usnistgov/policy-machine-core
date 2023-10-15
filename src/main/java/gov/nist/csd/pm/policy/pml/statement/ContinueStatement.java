package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.ContinueValue;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Objects;


public class ContinueStatement extends PMLStatement {
    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new ContinueValue();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ContinueStatement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + "continue";
    }
}
