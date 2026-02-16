package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.Objects;

public class ParenSubjectPatternExpression extends SubjectPatternExpression {

    private final SubjectPatternExpression expression;

    public ParenSubjectPatternExpression(SubjectPatternExpression expression) {
        this.expression = expression;
    }

    public SubjectPatternExpression getExpression() {
        return expression;
    }

    @Override
    public boolean matches(EventContextUser value, PolicyQuery query) throws PMException {
        return expression.matches(value, query);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "(" + expression.toFormattedString(indentLevel) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParenSubjectPatternExpression that)) return false;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }
}
