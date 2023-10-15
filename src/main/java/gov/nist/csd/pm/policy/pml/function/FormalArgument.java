package gov.nist.csd.pm.policy.pml.function;

import gov.nist.csd.pm.policy.pml.type.Type;

import java.io.Serializable;
import java.util.Objects;

public record FormalArgument(String name, Type type) implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormalArgument that = (FormalArgument) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
