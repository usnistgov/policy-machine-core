package gov.nist.csd.pm.policy.pml.value;

import gov.nist.csd.pm.policy.pml.type.Type;

public class BreakValue extends Value {
    public BreakValue() {
        super(Type.any());
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
    public String toString() {
        return "break";
    }
}
