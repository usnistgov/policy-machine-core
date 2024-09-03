package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.Objects;

public final class FormalOperand {
    private final String name;
    private final Type type;
    private final boolean isNodeop;

    public FormalOperand(String name, Type type, boolean isNodeop) {
        this.name = name;
        this.type = type;
        this.isNodeop = isNodeop;
    }

    public String name() {
        return name;
    }

    public Type type() {
        return type;
    }

    public boolean isNodeop() {
        return isNodeop;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FormalOperand) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.type, that.type) &&
                this.isNodeop == that.isNodeop;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, isNodeop);
    }

    @Override
    public String toString() {
        return "FormalOperand[" +
                "name=" + name + ", " +
                "type=" + type + ", " +
                "isNodeop=" + isNodeop + ']';
    }
}

