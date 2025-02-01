package gov.nist.csd.pm.pap.pml.pattern.operand;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.value.StringValue;

import java.util.Objects;
import java.util.Set;

public class InOperandPattern extends OperandPatternExpression {

    private String container;

    public InOperandPattern(StringLiteral container) {
        this.container = container.getValue();
    }


    public InOperandPattern(String container) {
        this.container = container;
    }

    @Override
    public boolean matches(String value, PAP pap) throws PMException {
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
        return "in " + new StringValue(container);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InOperandPattern that)) return false;
        return Objects.equals(container, that.container);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(container);
    }
}
