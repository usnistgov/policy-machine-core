/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
     * Get the kind (Java vs. PML-backed) of a persisted operation row, without materializing it.
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
