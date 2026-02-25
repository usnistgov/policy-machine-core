package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import java.util.Objects;

public class InSubjectPatternExpression extends SubjectPatternExpression {

    private final Expression<String> container;

    public InSubjectPatternExpression(Expression<String> container) {
        this.container = container;
    }

    @Override
    public boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        String containerName = container.execute(ctx, pap);

        long contId = pap.query().graph().getNodeId(containerName);

        if (user.isUser()) {
            long userId = pap.query().graph().getNodeId(user.getName());

            // check user contained in container
            return pap.query().graph().isAscendant(userId, contId);
        } else {
            // check if container in attrs
            return user.getAttrs().contains(containerName);
        }
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "user in " + container.toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSubjectPatternExpression that)) return false;
        return Objects.equals(container, that.container);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(container);
    }

}
