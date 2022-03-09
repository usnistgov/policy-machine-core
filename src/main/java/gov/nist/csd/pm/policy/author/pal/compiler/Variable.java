package gov.nist.csd.pm.policy.author.pal.compiler;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;

import java.util.Objects;

public record Variable(String name, Type type, boolean isConst) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name) && Objects.equals(type, variable.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
