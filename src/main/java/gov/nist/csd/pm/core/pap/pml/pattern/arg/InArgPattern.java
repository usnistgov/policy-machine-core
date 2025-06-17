package gov.nist.csd.pm.core.pap.pml.pattern.arg;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Objects;
import java.util.Set;

public class InArgPattern extends ArgPatternExpression {

    private final String container;

    public InArgPattern(StringLiteralExpression container) {
        this.container = container.getValue();
    }

    public InArgPattern(String container) {
        this.container = container;
    }

    @Override
    public boolean matchesInternal(String value, PAP pap) throws PMException {
        long valueId = pap.query().graph().getNodeId(value);
        long contId = pap.query().graph().getNodeId(container);

        return pap.query().graph().isAscendant(valueId, contId);
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
        if (!(o instanceof InArgPattern that)) return false;
        return Objects.equals(container, that.container);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(container);
    }
}
