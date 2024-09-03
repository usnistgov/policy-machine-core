package gov.nist.csd.pm.pap.pml.compiler;

import gov.nist.csd.pm.pap.pml.type.Type;

import java.io.Serializable;
import java.util.Objects;

public final class Variable {
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Variable) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.type, that.type) &&
                this.isConst == that.isConst;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, isConst);
    }

    @Override
    public String toString() {
        return "Variable[" +
                "name=" + name + ", " +
                "type=" + type + ", " +
                "isConst=" + isConst + ']';
    }
}
