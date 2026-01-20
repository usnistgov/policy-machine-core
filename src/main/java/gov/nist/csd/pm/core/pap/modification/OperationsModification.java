package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.Operation;

/**
 * Methods to manage NGAC resource and administrative operations.
 */
public interface OperationsModification {

    /**
     * Set the resource access rights for the policy.
     * @param resourceAccessRights The operations to set as the resource operations.
     * @throws PMException If there is an error in the PM.
     */
    void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException;

    /**
     * Create an operation.
     * @param operation The operation to create.
     * @throws PMException If there is an error in the PM.
     */
    void createOperation(Operation<?> operation) throws PMException;

    /**
     * Delete a resource, admin, routine, or query operation with the given name.
     * @param name The name of the operation to delete.
     * @throws PMException If there is an error in the PM.
     */
    void deleteOperation(String name) throws PMException;
}
