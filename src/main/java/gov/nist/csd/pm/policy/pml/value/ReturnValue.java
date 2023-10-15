package gov.nist.csd.pm.policy.pml.value;

import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.Objects;

public class ReturnValue extends Value{

    Value value;

    public ReturnValue(Value value) {
        super(value.getType());

        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReturnValue that = (ReturnValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ReturnValue{" +
                "value=" + value +
                '}';
    }
}
