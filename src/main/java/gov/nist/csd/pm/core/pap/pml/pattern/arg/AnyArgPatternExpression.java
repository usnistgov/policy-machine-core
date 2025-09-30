package gov.nist.csd.pm.core.pap.pml.pattern.arg;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Collection;
import java.util.List;

public class AnyArgPatternExpression extends ArgPatternExpression {

    @Override
    public boolean matchesInternal(String value, PAP pap) throws PMException {
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
        return obj instanceof AnyArgPatternExpression;
    }
}
