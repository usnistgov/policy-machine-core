package gov.nist.csd.pm.core.pap.pml.pattern.subject;

import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;
import java.util.Objects;
import java.util.Set;

public class UsernamePatternExpression extends SubjectPatternExpression {

    private final String user;

    public UsernamePatternExpression(StringLiteralExpression user) {
        this.user = user.getValue();
    }

    public UsernamePatternExpression(String user) {
        this.user = user;
    }

    @Override
    public boolean matchesInternal(EventContextUser value, PAP pap) throws PMException {
        return value.getName().equals(user);
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
        if (!(o instanceof UsernamePatternExpression that)) return false;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }
}
