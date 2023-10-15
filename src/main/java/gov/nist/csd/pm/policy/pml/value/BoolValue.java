package gov.nist.csd.pm.policy.pml.value;

import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.Objects;

public class BoolValue extends Value {

    private boolean value;

    public BoolValue(Boolean value) {
        super(Type.bool());
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
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
        BoolValue that = (BoolValue) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }
}
