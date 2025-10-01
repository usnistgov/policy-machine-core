package gov.nist.csd.pm.core.pap.pml.pattern.subject;

import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;

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
    public boolean matchesInternal(EventContextUser value, PAP pap) throws PMException {
        return value.getProcess().equals(process);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return new ReferencedNodes(false);
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
