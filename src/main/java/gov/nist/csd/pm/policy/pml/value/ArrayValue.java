package gov.nist.csd.pm.policy.pml.value;

import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.List;
import java.util.Objects;

public class ArrayValue extends Value {

    private List<Value> value;

    public ArrayValue(List<Value> value, Type elementType) {
        super(Type.array(elementType));
        this.value = value;
    }

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
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
        ArrayValue that = (ArrayValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Value value : value) {
            if (sb.length() != 0) {
                sb.append(", ");
            }

            sb.append(value);
        }

        return "[" + sb + "]";
    }
}
