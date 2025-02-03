package gov.nist.csd.pm.pap.pml.pattern.operand;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.pml.value.StringValue;

import java.util.Objects;
import java.util.Set;

public class NodeOperandPattern extends OperandPatternExpression {

    private final String node;

    public NodeOperandPattern(StringLiteral node) {
        this.node = node.getValue();
    }

    public NodeOperandPattern(String node) {
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
        return new StringValue(node).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeOperandPattern that)) return false;
        return Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(node);
    }
}
