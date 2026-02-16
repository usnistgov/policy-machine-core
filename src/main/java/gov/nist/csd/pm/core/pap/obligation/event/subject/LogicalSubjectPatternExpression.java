package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.Objects;

public class LogicalSubjectPatternExpression extends SubjectPatternExpression {

    private final SubjectPatternExpression left;
    private final SubjectPatternExpression right;
    private final boolean isAnd;

    public LogicalSubjectPatternExpression(SubjectPatternExpression left, SubjectPatternExpression right, boolean isAnd) {
        this.left = left;
        this.right = right;
        this.isAnd = isAnd;
    }

    public SubjectPattern getLeft() {
        return left;
    }

    public SubjectPattern getRight() {
        return right;
    }

    public boolean isAnd() {
        return isAnd;
    }

    @Override
    public boolean matches(EventContextUser value, PolicyQuery query) throws PMException {
        return isAnd ? left.matches(value, query) && right.matches(value, query)
                : left.matches(value, query) || right.matches(value, query);
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
