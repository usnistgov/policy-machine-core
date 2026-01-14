package gov.nist.csd.pm.core.pap.pml.pattern.subject;

import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;
import java.util.Objects;
import java.util.Set;

public class InSubjectPatternExpression extends SubjectPatternExpression {

    private final String container;

    public InSubjectPatternExpression(StringLiteralExpression container) {
        this.container = container.getValue();
    }

    public InSubjectPatternExpression(String container) {
        this.container = container;
    }

    @Override
    public boolean matchesInternal(EventContextUser value, PAP pap) throws PMException {
        long contId = pap.query().graph().getNodeId(container);

        if (value.isUser()) {
            long userId = pap.query().graph().getNodeId(value.getName());

            // check user contained in container
            return pap.query().graph().isAscendant(userId, contId);
        } else {
            // check if container in attrs
            return value.getAttrs().contains(container);
        }
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
        if (!(o instanceof InSubjectPatternExpression that)) return false;
        return Objects.equals(container, that.container);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(container);
    }

}
