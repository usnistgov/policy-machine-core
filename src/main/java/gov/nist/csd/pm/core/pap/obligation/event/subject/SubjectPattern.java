package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.io.Serializable;
import java.util.Objects;

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
     * @param value The value to check against this pattern.
     * @param query The PolicyQuery object to get policy information relevant to the value and pattern.
     * @return True if the value matches this pattern.
     */
    public boolean matches(EventContextUser value, PolicyQuery query) throws PMException {
        if (value == null) {
            return false;
        }

        return matchesInternal(value, query);
    }

    public boolean matchesInternal(EventContextUser value, PolicyQuery query) throws PMException {
        return isAny || subjectPatternExpression.matches(value, query);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return isAny ? "any user" : "user " + subjectPatternExpression.toFormattedString(indentLevel);
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
