package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import java.util.Objects;

public class NegateSubjectPatternExpression extends SubjectPatternExpression {

    private final SubjectPatternExpression subjectPatternExpression;

    public NegateSubjectPatternExpression(SubjectPatternExpression subjectPatternExpression) {
        this.subjectPatternExpression = subjectPatternExpression;
    }

    @Override
    public boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        return !subjectPatternExpression.matches(user, ctx, pap);
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
