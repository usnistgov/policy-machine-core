package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.Objects;

public class InSubjectPatternExpression extends SubjectPatternExpression {

    private final String container;

    public InSubjectPatternExpression(StringLiteralExpression container) {
        this.container = container.getValue();
    }

    public InSubjectPatternExpression(String container) {
        this.container = container;
    }

    @Override
    public boolean matches(EventContextUser value, PolicyQuery query) throws PMException {
        long contId = query.graph().getNodeId(container);

        if (value.isUser()) {
            long userId = query.graph().getNodeId(value.getName());

            // check user contained in container
            return query.graph().isAscendant(userId, contId);
        } else {
            // check if container in attrs
            return value.getAttrs().contains(container);
        }
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "in \"" + container + "\"";
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
