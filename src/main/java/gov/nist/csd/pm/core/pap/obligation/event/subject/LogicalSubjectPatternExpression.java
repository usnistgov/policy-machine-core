package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
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
    public boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        return isAnd ? left.matches(user, ctx, pap) && right.matches(user, ctx, pap)
                : left.matches(user, ctx,  pap) || right.matches(user, ctx, pap);
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
