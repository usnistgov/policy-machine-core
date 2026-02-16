package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.Objects;

public class ProcessSubjectPatternExpression extends SubjectPatternExpression {

    private final String process;

    public ProcessSubjectPatternExpression(StringLiteralExpression process) {
        this.process = process.getValue();
    }

    public ProcessSubjectPatternExpression(String process) {
        this.process = process;
    }

    @Override
    public boolean matches(EventContextUser value, PolicyQuery query) throws PMException {
        return value.getProcess().equals(process);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "process \"" + (process) + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessSubjectPatternExpression that)) return false;
        return Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(process);
    }
}
