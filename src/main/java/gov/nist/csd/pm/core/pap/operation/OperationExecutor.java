package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

public interface OperationExecutor {

    Object executeOperation(Operation<?> operation, UserContext userCtx, Args args) throws PMException;

}
