package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.exception.AdminAccessRightExistsException;
import gov.nist.csd.pm.common.exception.OperationExistsException;
import gov.nist.csd.pm.pap.modification.OperationsModification;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.store.PolicyStore;

import static gov.nist.csd.pm.pap.AdminAccessRights.isAdminAccessRight;
import static gov.nist.csd.pm.pap.AdminAccessRights.isWildcardAccessRight;

public class OperationsModifier extends Modifier implements OperationsModification {

    public OperationsModifier(PolicyStore store) {
        super(store);
    }

    @Override
    public void setResourceOperations(AccessRightSet resourceOperations) throws PMException {
        checkSetResourceAccessRightsInput(resourceOperations);

        store.operations().setResourceOperations(resourceOperations);
    }

    @Override
    public void createAdminOperation(Operation<?> operation) throws PMException {
        if (AdminOperations.isAdminOperation(operation.getName())
                || store.operations().getAdminOperationNames().contains(operation.getName())) {
            throw new OperationExistsException(operation.getName());
        }

        store.operations().createAdminOperation(operation);
    }

    @Override
    public void deleteAdminOperation(String operation) throws PMException {
        // return without error if the operation does not exist or is a built in admin op such as assign
        if (AdminOperations.ADMIN_OP_NAMES.contains(operation)
                || !store.operations().getAdminOperationNames().contains(operation)) {
            return;
        }

        store.operations().deleteAdminOperation(operation);
    }

    /**
     * Check that the provided resource operations are not existing admin access rights.
     *
     * @param accessRightSet The access right set to check.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkSetResourceAccessRightsInput(AccessRightSet accessRightSet) throws PMException {
        for (String ar : accessRightSet) {
            if (isAdminAccessRight(ar) || isWildcardAccessRight(ar)) {
                throw new AdminAccessRightExistsException(ar);
            }
        }
    }
}
