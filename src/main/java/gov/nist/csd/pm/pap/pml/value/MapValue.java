package gov.nist.csd.pm.pap.pml.value;

import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.Map;
import java.util.Objects;

public class MapValue extends Value {

    private Map<Value, Value> value;

    public MapValue(Map<Value, Value> value, Type keyType, Type valueType) {
        super(Type.map(keyType, valueType));
        this.value = value;
    }

    public Map<Value, Value> getValue() {
        return value;
    }

    public void setValue(Map<Value, Value> value) {
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
        MapValue mapValue = (MapValue) o;
        return Objects.equals(value, mapValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        StringBuilder entries = new StringBuilder();
        for (Map.Entry<Value, Value> entry : value.entrySet()) {
            if (entries.length() > 0) {
                entries.append(", ");
            }

            entries.append(entry.getKey()).append(": ").append(entry.getValue());
        }

        return String.format("{%s}", entries);
    }
}
