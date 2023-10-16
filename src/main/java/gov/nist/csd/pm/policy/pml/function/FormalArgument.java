package gov.nist.csd.pm.policy.pml.function;

import gov.nist.csd.pm.policy.pml.type.Type;

import java.io.Serializable;
import java.util.Objects;

public class FormalArgument implements Serializable {

    private final String name;
    private final Type type;

    public FormalArgument(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public Type type() {
        return type;
    }

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
