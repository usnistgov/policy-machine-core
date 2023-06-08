package gov.nist.csd.pm.pap.pml.value;

import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.Objects;

public class StringValue extends Value {

    private String value;

    public StringValue(String s) {
        super(Type.string());
        this.value = s;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
        StringValue that = (StringValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
