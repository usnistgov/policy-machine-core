package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.List;

public class AttributeHierarchy extends PMLStatement{

    String pc;

    List<Attribute> attrs;

    public AttributeHierarchy(List<Attribute> attrs) {
        this.attrs = attrs;
    }

    public List<Attribute> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<Attribute> attrs) {
        this.attrs = attrs;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return null;
    }
}