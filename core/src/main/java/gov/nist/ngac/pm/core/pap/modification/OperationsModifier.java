package gov.nist.ngac.pm.core.pap.modification;

import static gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightValidator.isAdminAccessRight;
import static gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightValidator.isWildcardAccessRight;

import gov.nist.ngac.pm.core.common.exception.AdminAccessRightExistsException;
import gov.nist.ngac.pm.core.common.exception.OperationExistsException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.NativeOperationRegistry;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;

public class OperationsModifier extends Modifier implements OperationsModification {

    private final NativeOperationRegistry nativeOperationRegistry;

    public OperationsModifier(PolicyStore store, NativeOperationRegistry nativeOperationRegistry) {
        super(store);
        this.nativeOperationRegistry = nativeOperationRegistry;
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
            // native operation: must already be registered (two-step register-then-create lifecycle);
            // return value ignored, get() throws OperationDoesNotExistException if unregistered
            nativeOperationRegistry.get(operation.getName());
        }

        policyStore.operations().createOperation(operation);
    }

    @Override
    public void deleteOperation(String name) throws PMException {
        if (nativeOperationRegistry.isProtected(name)) {
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
        return nativeOperationRegistry.isProtected(name)
            || policyStore.operations().operationExists(name);
    }

    public static class CannotDeleteProtectedOperationException extends PMException {

        public CannotDeleteProtectedOperationException(String name) {
            super("cannot delete protected operation " + name);
        }
    }
}
