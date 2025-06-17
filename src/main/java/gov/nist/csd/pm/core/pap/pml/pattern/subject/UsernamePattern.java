package gov.nist.csd.pm.core.pap.pml.pattern.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

import java.util.Objects;
import java.util.Set;

public class UsernamePattern extends SubjectPatternExpression {

    private final String user;

    public UsernamePattern(StringLiteralExpression user) {
        this.user = user.getValue();
    }

    public UsernamePattern(String user) {
        this.user = user;
    }

    @Override
    public boolean matchesInternal(String value, PAP pap) throws PMException {
        return value.equals(user);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return new ReferencedNodes(Set.of(user), false);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "\"" + user + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsernamePattern that)) return false;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }
}
