package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.value.BreakValue;
import gov.nist.csd.pm.pap.pml.value.Value;


public class BreakStatement extends ControlStatement {

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + "break";
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        return new BreakValue();
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
