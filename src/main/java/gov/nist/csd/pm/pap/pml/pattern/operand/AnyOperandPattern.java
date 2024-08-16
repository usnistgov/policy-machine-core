package gov.nist.csd.pm.pap.pml.pattern.operand;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;

import java.util.Collection;

public class AnyOperandPattern extends OperandPatternExpression {
    @Override
    public boolean matches(String value, PAP pap) throws PMException {
        return true;
    }

    @Override
    public boolean matches(Collection<String> value, PAP pap) throws PMException {
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
