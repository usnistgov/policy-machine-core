package gov.nist.csd.pm.pap.pml.value;

import gov.nist.csd.pm.pap.obligation.Rule;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ComplementedValue extends Value {

    private Value value;

    public ComplementedValue(Value value) {
        super(value.getType());
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return value.getStringValue();
    }

    @Override
    public Boolean getBooleanValue() {
        return !value.getBooleanValue();
    }

    @Override
    public List<Value> getArrayValue() {
        return value.getArrayValue();
    }

    @Override
    public Map<Value, Value> getMapValue() {
        return value.getMapValue();
    }

    @Override
    public Value getProhibitionValue() {
        return value.getProhibitionValue();
    }

    @Override
    public Rule getRuleValue() {
        return value.getRuleValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplementedValue that = (ComplementedValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "!" + value.toString();
    }
}
