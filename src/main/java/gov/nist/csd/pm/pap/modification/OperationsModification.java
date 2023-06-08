package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.op.Operation;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage NGAC resource and administrative operations.
 */
public interface OperationsModification {

    /**
     * Set the resource access rights for the policy.
     * @param resourceOperations The operations to set as the resource operations.
     * @throws PMException If there is an error in the PM.
     */
    void setResourceOperations(AccessRightSet resourceOperations) throws PMException;

    /**
     * Create an administrative operation.
     * @param operation The operation to create.
     * @throws PMException If there is an error in the PM.
     */
    void createAdminOperation(Operation<?> operation) throws PMException;

    /**
     * Delete the admin operation with the given name.
     * @param operation The operation name.
     * @throws PMException If there is an error in the PM.
     */
    void deleteAdminOperation(String operation) throws PMException;

}
