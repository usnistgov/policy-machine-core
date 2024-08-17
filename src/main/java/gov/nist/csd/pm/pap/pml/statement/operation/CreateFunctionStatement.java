package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;

public interface CreateFunctionStatement extends PMLStatement {

    PMLExecutableSignature getSignature();

}
