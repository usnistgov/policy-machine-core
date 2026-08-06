package gov.nist.ngac.pm.core.pap.obligation.event.operation;

/**
 * An {@link OperationPattern} that matches every operation.
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
