package gov.nist.csd.pm.pap.pml.statement.basic;

import gov.nist.csd.pm.pap.pml.statement.PMLStatement;

public abstract class BasicStatement implements PMLStatement {

    @Override
    public final String toString() {
        return toFormattedString(0);
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
} 