package gov.nist.csd.pm.core.pap.pml.pattern.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class InSubjectPattern extends SubjectPatternExpression {

    private final String container;

    public InSubjectPattern(StringLiteralExpression container) {
        this.container = container.getValue();
    }


    public InSubjectPattern(String container) {
        this.container = container;
    }

    @Override
    public boolean matchesInternal(String value, PAP pap) throws PMException {
        long valueId = pap.query().graph().getNodeId(value);
        long contId = pap.query().graph().getNodeId(container);

        return pap.query().graph().isAscendant(valueId, contId);
    }

    @Override
    public boolean matches(Collection<String> attrs, PAP pap) throws PMException {
        // if the specified container is contained in the attrs than the user is IN the container
        return attrs.contains(container);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return new ReferencedNodes(Set.of(container), false);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "in \"" + container + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSubjectPattern that)) return false;
        return Objects.equals(container, that.container);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(container);
    }

}
