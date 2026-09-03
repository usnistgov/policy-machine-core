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

package gov.nist.ngac.pm.core.pap.modification;

import static gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightValidator.isAdminAccessRight;
import static gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightValidator.isWildcardAccessRight;

import gov.nist.ngac.pm.core.common.exception.AdminAccessRightExistsException;
import gov.nist.ngac.pm.core.common.exception.OperationExistsException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.JavaOperationRegistry;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;

/**
 * {@link OperationsModification} implementation, validating operation and access right names before
 * delegating to the backend {@link gov.nist.ngac.pm.core.pap.store.OperationsStore} and the
 * {@link JavaOperationRegistry}.
 */
public class OperationsModifier extends Modifier implements OperationsModification {

    private final JavaOperationRegistry javaOperationRegistry;

    public OperationsModifier(PolicyStore store, JavaOperationRegistry javaOperationRegistry) {
        super(store);
        this.javaOperationRegistry = javaOperationRegistry;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
        checkSetResourceAccessRightsInput(resourceAccessRights);

        policyStore.operations().setResourceAccessRights(resourceAccessRights);
    }

    @Override
    public void createOperation(Operation<?> operation) throws PMException {
        if (operationExists(operation.getName())) {
            throw new OperationExistsException(operation.getName());
        }

        if (!(operation instanceof PMLOperation)) {
            // Java operation: must already be registered (two-step register-then-create lifecycle);
            // return value ignored, get() throws OperationDoesNotExistException if unregistered
            javaOperationRegistry.get(operation.getName());
        }

        policyStore.operations().createOperation(operation);
    }

    @Override
    public void deleteOperation(String name) throws PMException {
        if (javaOperationRegistry.isProtected(name)) {
            throw new CannotDeleteProtectedOperationException(name);
        } else if (!policyStore.operations().operationExists(name)) {
            return;
        }

        policyStore.operations().deleteOperation(name);
    }

    /**
     * Check that the provided resource operations are not existing admin access rights, operations or routines.
     */
    private void checkSetResourceAccessRightsInput(AccessRightSet accessRightSet) throws PMException {
        for (String ar : accessRightSet) {
            if (isAdminAccessRight(ar) || isWildcardAccessRight(ar) ) {
                throw new AdminAccessRightExistsException(ar);
            }
        }
    }

    /*
     * check if operation exists with the name
     */
    private boolean operationExists(String name) throws PMException {
        return javaOperationRegistry.isProtected(name)
            || policyStore.operations().operationExists(name);
    }

    public static class CannotDeleteProtectedOperationException extends PMException {

        public CannotDeleteProtectedOperationException(String name) {
            super("cannot delete protected operation " + name);
        }
    }
}
