package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.Objects;

public class NegateSubjectPatternExpression extends SubjectPatternExpression {

    private final SubjectPatternExpression subjectPatternExpression;

    public NegateSubjectPatternExpression(SubjectPatternExpression subjectPatternExpression) {
        this.subjectPatternExpression = subjectPatternExpression;
    }

    @Override
    public boolean matches(EventContextUser value, PolicyQuery query) throws PMException {
        return !subjectPatternExpression.matches(value, query);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "!" + subjectPatternExpression.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NegateSubjectPatternExpression that)) return false;
        return Objects.equals(subjectPatternExpression, that.subjectPatternExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(subjectPatternExpression);
    }
}
