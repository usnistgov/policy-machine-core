package gov.nist.csd.pm.pap.pml.pattern.subject;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;

import java.util.Objects;

public class SubjectPattern extends Pattern {

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

    @Override
    public boolean matches(String value, PAP pap) throws PMException {
        return isAny || subjectPatternExpression.matches(value, pap);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        if (isAny) {
            return new ReferencedNodes(true);
        }

        return subjectPatternExpression.getReferencedNodes();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return isAny ? "any user" : "user " + subjectPatternExpression.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectPattern)) return false;
        SubjectPattern that = (SubjectPattern) o;
        return isAny == that.isAny && Objects.equals(subjectPatternExpression, that.subjectPatternExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAny, subjectPatternExpression);
    }
}
