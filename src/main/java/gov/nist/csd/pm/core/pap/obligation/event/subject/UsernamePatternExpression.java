package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import java.util.Objects;

public class UsernamePatternExpression extends SubjectPatternExpression {

    private final Expression<String> user;

    public UsernamePatternExpression(Expression<String> user) {
        this.user = user;
    }

    @Override
    public boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        return user.getName().equals(this.user.execute(ctx, pap));
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
