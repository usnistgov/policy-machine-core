package gov.nist.csd.pm.core.pap.pml.pattern.subject;

import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Collection;
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
    public boolean matchesInternal(EventContextUser value, PAP pap) throws PMException {
        return expression.matches(value, pap);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return expression.getReferencedNodes();
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
