package gov.nist.csd.pm.core.pap.pml.statement.basic;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.result.BreakResult;

public class BreakStatement extends BasicStatement<BreakResult> {

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + "break";
    }

    @Override
    public BreakResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        return new BreakResult();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BreakStatement;
    }
} 