package gov.nist.ngac.pm.core.pap.query;

import gov.nist.ngac.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.OperationKind;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Collection;

/**
 * Interface to query operations.
 */
public interface OperationsQuery {

    /**
     * Get the resource operations that have been set for the policy.
     * @return An AccessRightSet containing the resource operations.
     * @throws PMException If there is an error in the PM.
     */
    AccessRightSet getResourceAccessRights() throws PMException;

    /**
     * Get all operations.
     * @return A collection of all operations.
     * @throws PMException If there is an error in the PM.
     */
    Collection<Operation<?>> getOperations() throws PMException;

    /**
     * Get all operation names.
     * @return A collection of all the operation names.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getOperationNames() throws PMException;

    /**
     * Get the operation with the given name.
     * @param name The name of the operation to get.
     * @return The Operation object.
     * @throws PMException If there is an error in the PM.
     */
    Operation<?> getOperation(String name) throws PMException;

    /**
     * Get the kind (native vs. PML-backed) of a persisted operation row, without materializing it.
     * @param name The name of the operation.
     * @return The row's {@link OperationKind}.
     * @throws OperationDoesNotExistException If no operation with this name is persisted.
     * @throws PMException If there is an error in the PM.
     */
    OperationKind getOperationKind(String name) throws PMException;

    /**
     * Returns true if the given name matches any resource operation, admin operation, or routine.
     * @param name The name of the operation.
     * @return True if the given name matches an existing operation.
     * @throws PMException If there is an error in the PM.
     */
    boolean operationExists(String name) throws PMException;
}
