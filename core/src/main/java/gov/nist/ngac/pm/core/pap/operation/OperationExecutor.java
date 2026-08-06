package gov.nist.ngac.pm.core.pap.operation;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;

/**
 * Executes an {@link Operation} with a given set of arguments, as implemented by {@link
 * gov.nist.ngac.pm.core.pap.PAP}.
 */
public interface OperationExecutor {

    /**
     * Executes the given operation.
     *
     * @param operation the operation to execute
     * @param userCtx the user executing the operation
     * @param args the operation's arguments
     * @return the operation's return value
     * @throws PMException if execution fails
     */
    Object executeOperation(Operation<?> operation, UserContext userCtx, Args args) throws PMException;

}
