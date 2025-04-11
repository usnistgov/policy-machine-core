package gov.nist.csd.pm.pap.pml.compiler;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;

import java.io.Serializable;
import java.util.Objects;

public record Variable(String name, ArgType<?> type, boolean isConst) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return isConst == variable.isConst && Objects.equals(name, variable.name) && Objects.equals(type, variable.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
