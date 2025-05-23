package gov.nist.csd.pm.core.pap.pml.pattern.arg;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Objects;
import java.util.Set;

public class NodeArgPattern extends ArgPatternExpression {

    private final String node;

    public NodeArgPattern(StringLiteralExpression node) {
        this.node = node.getValue();
    }

    public NodeArgPattern(String node) {
        this.node = node;
    }

    @Override
    public boolean matches(String value, PAP pap) throws PMException {
        return value.equals(node);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return new ReferencedNodes(Set.of(node), false);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "\"" + node + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeArgPattern that)) return false;
        return Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(node);
    }
}
