package gov.nist.csd.pm.core.pap.pml.pattern.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Objects;

public class NegateSubjectPatternExpression extends SubjectPatternExpression {

    private final Pattern subjectPatternExpression;

    public NegateSubjectPatternExpression(Pattern subjectPatternExpression) {
        this.subjectPatternExpression = subjectPatternExpression;
    }

    public Pattern getSubjectPatternExpression() {
        return subjectPatternExpression;
    }

    @Override
    public boolean matchesInternal(String value, PAP pap) throws PMException {
        return !subjectPatternExpression.matches(value, pap);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return subjectPatternExpression.getReferencedNodes();
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
