package gov.nist.csd.pm.pap.pml.value;

import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.Objects;

public class RuleValue extends Value {

    private Rule value;

    public RuleValue(Rule value) {
        super(Type.any());

        this.value = value;
    }

    public Rule getValue() {
        return value;
    }

    public void setValue(Rule value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleValue ruleValue = (RuleValue) o;
        return Objects.equals(value, ruleValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
