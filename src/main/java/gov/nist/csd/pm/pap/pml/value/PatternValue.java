package gov.nist.csd.pm.pap.pml.value;

import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.Objects;

public class PatternValue extends Value{

    private Pattern value;

    public PatternValue(Pattern value) {
        super(Type.any());
        this.value = value;
    }

    public Pattern getValue() {
        return value;
    }

    public void setValue(Pattern value) {
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
        PatternValue that = (PatternValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "PatternValue{" +
                "pattern=" + value +
                '}';
    }
}
