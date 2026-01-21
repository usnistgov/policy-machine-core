package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;

public interface OperationExecutor {

    Object executeOperation(Operation<?> operation, Args args) throws PMException;

}
