package gov.nist.csd.pm.pap.pml.value;

import gov.nist.csd.pm.pap.pml.type.Type;

public class ContinueValue extends Value {
    public ContinueValue() {
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
        return null;
    }
}
