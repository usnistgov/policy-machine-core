package gov.nist.csd.pm.policy.pml.compiler;

import gov.nist.csd.pm.policy.pml.type.Type;

import java.io.Serializable;
import java.util.Objects;

public class Variable implements Serializable {

    private final String name;
    private final Type type;
    private final boolean isConst;

    public Variable(String name, Type type, boolean isConst) {
        this.name = name;
        this.type = type;
        this.isConst = isConst;
    }

    public String name() {
        return name;
    }

    public Type type() {
        return type;
    }

    public boolean isConst() {
        return isConst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Variable variable = (Variable) o;
        return isConst == variable.isConst && Objects.equals(name, variable.name) && Objects.equals(
                type, variable.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, isConst);
    }
}
