package gov.nist.ngac.pm.core.pap.obligation.event.subject;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import java.util.Objects;

/**
 * PML user &lt;name&gt; subject pattern. Matches an event whose acting user's name equals the given name.
 */
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
        return "user " + user.toFormattedString(0);
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
