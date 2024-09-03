package gov.nist.csd.pm.pap.pml.pattern.subject;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.value.StringValue;

import java.util.Objects;
import java.util.Set;

public class InSubjectPattern extends SubjectPatternExpression {

    private String container;

    public InSubjectPattern(StringLiteral container) {
        this.container = container.getValue();
    }


    public InSubjectPattern(String container) {
        this.container = container;
    }

    @Override
    public boolean matches(String value, PAP pap) throws PMException {
        return pap.query().graph().isAscendant(value, container);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return new ReferencedNodes(Set.of(container), false);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "in " + new StringValue(container);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSubjectPattern)) return false;
        InSubjectPattern that = (InSubjectPattern) o;
        return Objects.equals(container, that.container);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(container);
    }
}
