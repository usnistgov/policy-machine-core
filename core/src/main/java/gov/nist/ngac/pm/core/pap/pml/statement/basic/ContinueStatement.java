package gov.nist.ngac.pm.core.pap.pml.statement.basic;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.statement.result.ContinueResult;

/**
 * PML "continue" statement, signaling a {@link ForeachStatement} to skip to the next iteration.
 */
public class ContinueStatement extends BasicStatement<ContinueResult> {

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + "continue";
    }

    @Override
    public ContinueResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        return new ContinueResult();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ContinueStatement;
    }
} 