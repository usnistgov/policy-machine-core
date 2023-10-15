package gov.nist.csd.pm.policy.pml.value;

import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.Objects;

public class VoidValue extends Value {
    public VoidValue() {
        super(Type.voidType());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof VoidValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    @Override
    public String toString() {
        return "void";
    }
}
