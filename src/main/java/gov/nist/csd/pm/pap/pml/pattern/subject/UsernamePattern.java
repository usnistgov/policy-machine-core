package gov.nist.csd.pm.pap.pml.pattern.subject;

import gov.nist.csd.pm.common.event.operand.StringOperandValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.value.StringValue;

import java.util.Objects;
import java.util.Set;

public class UsernamePattern extends SubjectPatternExpression {

    private String user;

    public UsernamePattern(StringLiteral user) {
        this.user = user.getValue();
    }

    public UsernamePattern(String user) {
        this.user = user;
    }

    @Override
    public boolean matches(StringOperandValue value, PAP pap) throws PMException {
        return value.equals(user);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return new ReferencedNodes(Set.of(user), false);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return new StringValue(user).toString();
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
