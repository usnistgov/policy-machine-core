package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.value.Value;

public interface PMLStatementExecutable {

    Value execute(ExecutionContext ctx, PAP pap) throws PMException;

}
