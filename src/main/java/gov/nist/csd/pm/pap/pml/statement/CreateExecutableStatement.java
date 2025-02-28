package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;

public interface CreateExecutableStatement extends PMLStatement {

    PMLExecutableSignature getSignature();

}
