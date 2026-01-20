package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.BasicFunction;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;

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
     * Create an administrative operation.
     * @param operation The operation to create.
     * @throws PMException If there is an error in the PM.
     */
    void createAdminOperation(AdminOperation<?> operation) throws PMException;

    /**
     * Create a new administrative routine.
     * @param routine The routine to create.
     * @throws PMException If there is an error in the PM.
     */
    void createAdminRoutine(Routine<?> routine) throws PMException;

    /**
     * Create a query operation.
     * @param operation The query operation to create.
     * @throws PMException If there is an error in the PM.
     */
    void createQueryOperation(QueryOperation<?> operation) throws PMException;

    /**
     * Create a basic function.
     * @param function The basic function to create.
     * @throws PMException If there is an error in the PM.
     */
    void createBasicFunction(BasicFunction<?> function) throws PMException;

    /**
     * Delete a resource, admin, routine, or query operation with the given name.
     * @param name The name of the operation to delete.
     * @throws PMException If there is an error in the PM.
     */
    void deleteOperation(String name) throws PMException;
}
