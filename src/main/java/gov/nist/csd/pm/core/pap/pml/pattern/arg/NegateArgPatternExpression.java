package gov.nist.csd.pm.core.pap.pml.pattern.arg;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Objects;

public class NegateArgPatternExpression extends ArgPatternExpression {

    private final Pattern argPatternExpression;

    public NegateArgPatternExpression(Pattern argPatternExpression) {
        this.argPatternExpression = argPatternExpression;
    }

    public Pattern getArgPatternExpression() {
        return argPatternExpression;
    }

    @Override
    public boolean matchesInternal(String value, PAP pap) throws PMException {
        return !argPatternExpression.matches(value, pap);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return argPatternExpression.getReferencedNodes();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "!" + argPatternExpression.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NegateArgPatternExpression that)) return false;
        return Objects.equals(argPatternExpression, that.argPatternExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(argPatternExpression);
    }
}
