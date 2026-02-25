package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.Objects;

public class ProcessSubjectPatternExpression extends SubjectPatternExpression {

    private final Expression<String> process;

    public ProcessSubjectPatternExpression(Expression<String> process) {
        this.process = process;
    }

    @Override
    public boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        return user.getProcess().equals(this.process.execute(ctx, pap));
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "process " + process.toFormattedString(0);
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
