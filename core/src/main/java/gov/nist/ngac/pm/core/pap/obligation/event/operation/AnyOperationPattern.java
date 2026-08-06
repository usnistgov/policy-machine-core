package gov.nist.ngac.pm.core.pap.obligation.event.operation;

/**
 * PML "any operation" pattern: matches every operation.
 */
public final class AnyOperationPattern extends OperationPattern {

    @Override
    public String toFormattedString(int indentLevel) {
        return "any operation";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnyOperationPattern;
    }
}
