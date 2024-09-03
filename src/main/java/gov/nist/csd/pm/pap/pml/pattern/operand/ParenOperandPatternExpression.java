package gov.nist.csd.pm.pap.pml.pattern.operand;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;

import java.util.Objects;

public class ParenOperandPatternExpression extends OperandPatternExpression {

    private Pattern expression;

    public ParenOperandPatternExpression(Pattern expression) {
        this.expression = expression;
    }

    public Pattern getExpression() {
        return expression;
    }

    @Override
    public boolean matches(String value, PAP pap) throws PMException {
        return expression.matches(value, pap);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return expression.getReferencedNodes();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "(" + expression.toFormattedString(indentLevel) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParenOperandPatternExpression)) return false;
        ParenOperandPatternExpression that = (ParenOperandPatternExpression) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }
}
