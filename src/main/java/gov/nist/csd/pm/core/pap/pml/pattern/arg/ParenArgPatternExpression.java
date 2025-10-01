package gov.nist.csd.pm.core.pap.pml.pattern.arg;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Objects;

public class ParenArgPatternExpression extends ArgPatternExpression {

    private final ArgPatternExpression expression;

    public ParenArgPatternExpression(ArgPatternExpression expression) {
        this.expression = expression;
    }

    public ArgPatternExpression getExpression() {
        return expression;
    }

    @Override
    public boolean matchesInternal(String value, PAP pap) throws PMException {
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
        if (!(o instanceof ParenArgPatternExpression that)) return false;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }
}
