package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.value.ContinueValue;
import gov.nist.csd.pm.pap.pml.value.Value;


public class ContinueStatement extends ControlStatement {

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + "continue";
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        return new ContinueValue();
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
