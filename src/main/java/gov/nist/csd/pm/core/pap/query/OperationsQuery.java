package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.BasicFunction;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;
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
     * Get the names of all resource operations.
     * @return A collection of all the resource operation names.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getResourceOperationNames() throws PMException;

    /**
     * Get the resource operation with the given name.
     * @param operationName The operation name.
     * @return The ResourceOperation object.
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
     * Get the query operation names.
     * @return A list of the query operation names.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getQueryOperationNames() throws PMException;

    /**
     * Get the query operation with the given name.
     * @param name The query operation name.
     * @return The QueryOperation object.
     * @throws PMException If there is an error in the PM.
     */
    QueryOperation<?> getQueryOperation(String name) throws PMException;

    /**
     * Get the basic function names.
     * @return A list of the basic function names.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getBasicFunctionNames() throws PMException;

    /**
     * Get the basic function with the given name.
     * @param name The basic function name.
     * @return The BasicFunction object.
     * @throws PMException If there is an error in the PM.
     */
    BasicFunction<?> getBasicFunction(String name) throws PMException;

    /**
     * Returns true if the given name matches any resource operation, admin operation, or routine stored in the policy.
     * @param operationName The name of the operation.
     * @return True if the given name matches an existing operation.
     * @throws PMException If there is an error in the PM.
     */
    boolean operationExists(String operationName) throws PMException;
}
