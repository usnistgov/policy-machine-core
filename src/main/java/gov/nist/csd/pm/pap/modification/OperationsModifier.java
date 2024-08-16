package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.exception.AdminAccessRightExistsException;
import gov.nist.csd.pm.pap.exception.OperationExistsException;
import gov.nist.csd.pm.pap.op.AdminOperations;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.store.PolicyStore;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.isAdminAccessRight;
import static gov.nist.csd.pm.pap.op.AdminAccessRights.isWildcardAccessRight;

public class OperationsModifier extends Modifier implements OperationsModification{

    public OperationsModifier(PolicyStore store) throws PMException {
        super(store);
        AdminOperations.init(store.operations());
    }

    public OperationsModifier(Modifier modifier) throws PMException {
        super(modifier);
        AdminOperations.init(store.operations());
    }

    @Override
    public void setResourceOperations(AccessRightSet resourceOperations) throws PMException {
        checkSetResourceAccessRightsInput(resourceOperations);

        store.operations().setResourceOperations(resourceOperations);
    }

    @Override
    public void createAdminOperation(Operation<?> operation) throws PMException {
        if (store.operations().getAdminOperationNames().contains(operation.getName())) {
            throw new OperationExistsException(operation.getName());
        }

        store.operations().createAdminOperation(operation);
    }

    @Override
    public void deleteAdminOperation(String operation) throws PMException {
        // return without error if the operation does not exist or is a built in admin op such as assign
        if (!store.operations().getAdminOperationNames().contains(operation) ||
                AdminOperations.ADMIN_OP_NAMES.contains(operation)) {
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
