package gov.nist.csd.pm.core.pap.obligation.event.operation;

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
