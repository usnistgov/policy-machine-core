package gov.nist.csd.pm.pap.pml.pattern.operand;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;

import java.util.Objects;

public class NegateOperandPatternExpression extends OperandPatternExpression {

    private final Pattern operandPatternExpression;

    public NegateOperandPatternExpression(Pattern operandPatternExpression) {
        this.operandPatternExpression = operandPatternExpression;
    }

    public Pattern getOperandPatternExpression() {
        return operandPatternExpression;
    }

    @Override
    public boolean matches(String value, PAP pap) throws PMException {
        return !operandPatternExpression.matches(value, pap);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return operandPatternExpression.getReferencedNodes();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "!" + operandPatternExpression.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NegateOperandPatternExpression that)) return false;
        return Objects.equals(operandPatternExpression, that.operandPatternExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(operandPatternExpression);
    }
}
