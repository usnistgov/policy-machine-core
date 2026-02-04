package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.Operation;
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
     * Returns true if the given name matches any resource operation, admin operation, or routine.
     * @param name The name of the operation.
     * @return True if the given name matches an existing operation.
     * @throws PMException If there is an error in the PM.
     */
    boolean operationExists(String name) throws PMException;
}
