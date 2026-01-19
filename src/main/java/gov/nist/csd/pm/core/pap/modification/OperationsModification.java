package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.Routine;

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
     * Create a resource operation.
     * @param operation The operation to create.
     * @throws PMException If there is an error in the PM.
     */
    void createResourceOperation(ResourceOperation<?> operation) throws PMException;

    /**
     * Delete the admin operation with the given name.
     * @param operation The operation name.
     * @throws PMException If there is an error in the PM.
     */
    void deleteResourceOperation(String operation) throws PMException;

    /**
     * Create an administrative operation.
     * @param operation The operation to create.
     * @throws PMException If there is an error in the PM.
     */
    void createAdminOperation(AdminOperation<?> operation) throws PMException;

    /**
     * Delete the admin operation with the given name.
     * @param operation The operation name.
     * @throws PMException If there is an error in the PM.
     */
    void deleteAdminOperation(String operation) throws PMException;

    /**
     * Create a new administrative routine.
     * @param routine The routine to create.
     * @throws PMException If there is an error in the PM.
     */
    void createAdminRoutine(Routine<?> routine) throws PMException;

    /**
     * Delete the admin routine with the given name.
     * @param name The name of the admin routine to delete.
     * @throws PMException If there is an error in the PM.
     */
    void deleteAdminRoutine(String name) throws PMException;
}
