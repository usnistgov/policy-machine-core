package gov.nist.csd.pm.pap.pml.value;

import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.Objects;

public class ProhibitionValue extends Value {

    private Prohibition value;

    public ProhibitionValue(Prohibition value) {
        super(Type.any());

        this.value = value;
    }

    public Value getValue() {
        return Value.fromObject(value);
    }

    public void setValue(Prohibition value) {
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
        ProhibitionValue that = (ProhibitionValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
