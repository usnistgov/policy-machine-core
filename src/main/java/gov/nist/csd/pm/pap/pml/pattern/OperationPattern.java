package gov.nist.csd.pm.pap.pml.pattern;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.util.Objects;

public class OperationPattern extends Pattern {

    private final String value;
    private final boolean isAny;

    public OperationPattern(String value) {
        this.value = value;
        this.isAny = false;
    }

    public OperationPattern() {
        this.isAny = true;
        this.value = null;
    }

    public String getValue() {
        return value;
    }

    public boolean isAny() {
        return isAny;
    }

    @Override
    public boolean matches(String value, PAP pap) throws PMException {
        return isAny || value.equals(this.value);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return new ReferencedNodes(false);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return isAny ? "any operation" : value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationPattern that)) return false;
        return isAny == that.isAny && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isAny);
    }
}
