package gov.nist.csd.pm.pap.pml.statement;

public abstract class ControlStatement implements PMLStatement{

    @Override
    public final String toString() {
        return toFormattedString(0);
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}
