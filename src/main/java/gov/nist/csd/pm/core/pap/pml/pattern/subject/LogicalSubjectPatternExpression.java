package gov.nist.csd.pm.core.pap.pml.pattern.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Collection;
import java.util.Objects;

public class LogicalSubjectPatternExpression extends SubjectPatternExpression {

    private final Pattern left;
    private final Pattern right;
    private final boolean isAnd;

    public LogicalSubjectPatternExpression(Pattern left, Pattern right, boolean isAnd) {
        this.left = left;
        this.right = right;
        this.isAnd = isAnd;
    }

    public Pattern getLeft() {
        return left;
    }

    public Pattern getRight() {
        return right;
    }

    public boolean isAnd() {
        return isAnd;
    }

    @Override
    public boolean matchesInternal(String value, PAP pap) throws PMException {
        return isAnd ? left.matches(value, pap) && right.matches(value, pap)
                : left.matches(value, pap) || right.matches(value, pap);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return left.getReferencedNodes().combine(right.getReferencedNodes());
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toFormattedString(indentLevel) +
                (isAnd ? " && " : " || ") +
                right.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalSubjectPatternExpression that)) return false;
        return isAnd == that.isAnd && Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isAnd);
    }
}
