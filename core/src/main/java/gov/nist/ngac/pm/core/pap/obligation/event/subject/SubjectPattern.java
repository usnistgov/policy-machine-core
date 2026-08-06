package gov.nist.ngac.pm.core.pap.obligation.event.subject;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementSerializable;
import java.io.Serializable;
import java.util.Objects;

/**
 * The subject part of an event pattern, matching either any user or a specific subject-pattern expression.
 */
public class SubjectPattern implements Serializable, PMLStatementSerializable {

    private final boolean isAny;
    private final SubjectPatternExpression subjectPatternExpression;

    public SubjectPattern() {
        this.isAny = true;
        this.subjectPatternExpression = null;
    }

    public SubjectPattern(SubjectPatternExpression subjectPatternExpression) {
        this.isAny = false;
        this.subjectPatternExpression = subjectPatternExpression;
    }

    public boolean isAny() {
        return isAny;
    }

    public SubjectPatternExpression getSubjectPatternExpression() {
        return subjectPatternExpression;
    }

    /**
     * Returns true if the given value matches this pattern. If the value is null, then return false.
     *
     * @param user the user.
     * @param ctx the event context information.
     * @param pap  The PolicyQuery object to get policy information relevant to the value and pattern.
     * @return True if the value matches this pattern.
     */
    public boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        if (user == null) {
            return false;
        }

        return matchesInternal(user, ctx, pap);
    }

    /**
     * The match logic behind {@link #matches}, run once the user is already confirmed non-null.
     */
    public boolean matchesInternal(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        return isAny || subjectPatternExpression.matches(user, ctx, pap);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return isAny ? "any user" : subjectPatternExpression.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectPattern that)) return false;
        return isAny == that.isAny && Objects.equals(subjectPatternExpression, that.subjectPatternExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAny, subjectPatternExpression);
    }
}
