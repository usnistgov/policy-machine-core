package gov.nist.csd.pm.pap.pml.pattern.operand;

import gov.nist.csd.pm.common.event.operand.ListStringOperandValue;
import gov.nist.csd.pm.common.event.operand.StringOperandValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;

public class AnyOperandPattern extends OperandPatternExpression {
    @Override
    public boolean matches(StringOperandValue value, PAP pap) throws PMException {
        return true;
    }

    @Override
    public boolean matches(ListStringOperandValue value, PAP pap) throws PMException {
        return true;
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return new ReferencedNodes(true);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "any";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnyOperandPattern;
    }
}
