package gov.nist.csd.pm.pap.pml.pattern.subject;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.value.StringValue;

import java.util.Objects;

public class ProcessSubjectPattern extends SubjectPatternExpression {

    private String process;

    public ProcessSubjectPattern(StringLiteral process) {
        this.process = process.getValue();
    }

    public ProcessSubjectPattern(String process) {
        this.process = process;
    }

    @Override
    public boolean matches(String value, PAP pap) throws PMException {
        return value.equals(process);
    }

    @Override
    public ReferencedNodes getReferencedNodes() {
        return new ReferencedNodes(false);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "process " + new StringValue(process);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessSubjectPattern that)) return false;
        return Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(process);
    }
}
