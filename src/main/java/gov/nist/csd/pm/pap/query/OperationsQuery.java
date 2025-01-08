package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.op.Operation;

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
    AccessRightSet getResourceOperations() throws PMException;

    /**
     * Get the names of all admin operations.
     * @return A collection of all the admin operation names.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getAdminOperationNames() throws PMException;

    /**
     * Get the admin operation with the given name.
     * @param operationName The operation name.
     * @return The Operation object.
     * @throws PMException If there is an error in the PM.
     */
    Operation<?> getAdminOperation(String operationName) throws PMException;

}
