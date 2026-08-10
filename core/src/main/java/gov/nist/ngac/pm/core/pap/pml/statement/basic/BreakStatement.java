package gov.nist.ngac.pm.core.pap.pml.statement.basic;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.statement.result.BreakResult;

/**
 * A PML break statement that stops the enclosing loop's iteration.
 */
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