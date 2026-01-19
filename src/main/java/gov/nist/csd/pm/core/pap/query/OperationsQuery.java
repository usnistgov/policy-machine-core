package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
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
     * Get the names of all admin operations.
     * @return A collection of all the admin operation names.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getResourceOperationNames() throws PMException;

    /**
     * Get the admin operation with the given name.
     * @param operationName The operation name.
     * @return The Operation object.
     * @throws PMException If there is an error in the PM.
     */
    ResourceOperation<?> getResourceOperation(String operationName) throws PMException;

    /**
     * Get the names of all admin operations.
     * @return A collection of all the admin operation names.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getAdminOperationNames() throws PMException;

    /**
     * Get the admin operation with the given name.
     *
     * @param operationName The operation name.
     * @return The Operation object.
     * @throws PMException If there is an error in the PM.
     */
    AdminOperation<?> getAdminOperation(String operationName) throws PMException;

    /**
     * Get the names of all admin routines.
     * @return A collection of all the admin routine names.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getAdminRoutineNames() throws PMException;

    /**
     * Get the admin operation with the given name.
     *
     * @param routineName The routine name.
     * @return The Routine object.
     * @throws PMException If there is an error in the PM.
     */
    Routine<?> getAdminRoutine(String routineName) throws PMException;

    /**
     * Returns true if the given name matches any resource operation, admin operation, or routine stored in the policy.
     * @param operationName The name of the operation.
     * @return True if the given name matches an existing operation.
     * @throws PMException If there is an error in the PM.
     */
    boolean operationExists(String operationName) throws PMException;
}
