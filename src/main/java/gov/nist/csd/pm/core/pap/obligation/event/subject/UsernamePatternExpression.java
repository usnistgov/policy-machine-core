package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.Objects;

public class UsernamePatternExpression extends SubjectPatternExpression {

    private final String user;

    public UsernamePatternExpression(StringLiteralExpression user) {
        this.user = user.getValue();
    }

    public UsernamePatternExpression(String user) {
        this.user = user;
    }

    @Override
    public boolean matches(EventContextUser value, PolicyQuery query) throws PMException {
        return value.getName().equals(user);
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
