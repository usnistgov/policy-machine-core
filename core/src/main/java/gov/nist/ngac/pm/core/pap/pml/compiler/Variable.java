package gov.nist.ngac.pm.core.pap.pml.compiler;

import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import java.io.Serializable;
import java.util.Objects;

/**
 * A compile-time record of a PML variable's name, static type, and whether it is a constant.
 */
public record Variable(String name, Type<?> type, boolean isConst) implements Serializable {

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
